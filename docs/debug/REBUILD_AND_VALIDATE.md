# Rebuild y validación tras fix de sesión

## 1. Rebuild limpio (obligatorio)

```powershell
docker compose down -v
docker compose build --no-cache
docker compose up -d
```

Comprobar que todo está arriba:

```powershell
docker compose ps
```

## 2. Validaciones de aceptación

### 2.1 Redis debe tener keys de sesión

```powershell
curl.exe -sk -c cookies.txt "https://localhost/session?value=sesion-ok"
docker exec mockup-redis redis-cli KEYS "mockup:sessions*"
```

**Criterio:** La salida de `KEYS` no debe ser vacía.

### 2.2 Prueba funcional (8 requests)

```powershell
1..8 | ForEach-Object { curl.exe -sk -b cookies.txt "https://localhost/whoami"; "" }
```

**Criterios:**
- `instanceId` cambia (app1, app2, app3)
- `sessionValue` siempre `"sesion-ok"`
- `sessionId` siempre el mismo

### 2.3 /whoami no debe emitir set-cookie nuevo

```powershell
1..8 | ForEach-Object {
  curl.exe -vk -b cookies.txt https://localhost/whoami 2>&1 |
    Select-String "instanceId|set-cookie|Cookie:|sessionValue|sessionId"
  "----"
}
```

**Criterio:** No deben aparecer líneas `< set-cookie:`.

Guardar evidencias en:
- `docs/debug/session_after.txt`
- `docs/debug/session_after_headers.txt`

## 3. Si falla: diagnóstico

Buscar errores de Spring Session en logs:

```powershell
docker compose logs --tail=400 app1 app2 app3 | Select-String -Pattern "Spring Session|SessionRepository|RedisIndexedSessionRepository|Exception|ERROR"
```

Comprobar conectividad de las apps a Redis:

```powershell
docker exec mockup-redis redis-cli PING
docker exec mockup-redis redis-cli SET codex_test ok
docker exec mockup-redis redis-cli GET codex_test
docker exec mockup-redis redis-cli DEL codex_test
```
