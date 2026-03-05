# Debug de sesión distribuida (Redis)

## Baseline (antes del fix)

Con el entorno levantado (`docker compose up -d`), ejecutar y guardar salida:

```powershell
# Crear sesión y 8 whoami
curl.exe -sk -c cookies.txt "https://localhost/session?value=sesion-ok"
1..8 | ForEach-Object { curl.exe -sk -b cookies.txt "https://localhost/whoami"; "" }
# Guardar output en session_before.txt

# Verificar set-cookie en headers
1..8 | ForEach-Object {
  curl.exe -vk -b cookies.txt https://localhost/whoami 2>&1 |
    Select-String "instanceId|set-cookie|Cookie:|sessionValue|sessionId"
  "----"
}
# Guardar output en session_before_headers.txt
```

## Después del fix

Mismas pruebas; guardar en `session_after.txt` y `session_after_headers.txt`.

Criterios de éxito:
- Redis tiene keys `mockup:sessions*` tras hit a `/session`
- `sessionId` constante, `sessionValue` siempre `sesion-ok`
- No debe aparecer `set-cookie:` en respuestas de `/whoami` al rotar instancia
