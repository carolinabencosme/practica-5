# Guía paso a paso para comprobar que TODO funciona

Esta guía está pensada para validar los requisitos de la práctica (HAProxy + HTTPS + Round Robin + failover + sesiones distribuidas con Redis).

---

## 0) Requisitos previos

Asegúrate de tener instalado:

- Docker Engine
- Docker Compose v2 (`docker compose version`)
- `curl`

Comprobación rápida:

```bash
docker --version
docker compose version
curl --version
```

---

## 1) Levantar el entorno

Desde la raíz del proyecto:

```bash
docker compose down -v
docker compose up -d --build
```

Verifica contenedores:

```bash
docker compose ps
```

Debes ver al menos estos servicios **Up**:

- `mockup-haproxy`
- `mockup-app1`
- `mockup-app2`
- `mockup-app3`
- `mockup-redis`
- `mockup-mysql`

Si alguno no está en `Up`, revisa logs:

```bash
docker compose logs --tail=100 haproxy app1 app2 app3 redis db
```

---

## 2) Verificar redirección HTTP -> HTTPS

Ejecuta:

```bash
curl -I http://localhost
```

Resultado esperado:

- Código `301`
- Header `Location: https://localhost/...`

---

## 3) Verificar health checks y backend en HAProxy

Abre en navegador:

- `http://localhost:8404/stats`

Resultado esperado:

- Backend `app_back`
- `app1`, `app2`, `app3` en estado **UP**

También puedes validar health endpoint directamente por HAProxy:

```bash
curl -k https://localhost/actuator/health
```

Resultado esperado:

- JSON con estado `UP`

---

## 4) Verificar Round Robin (balanceo)

Ejecuta varias veces:

```bash
for i in {1..10}; do curl -sk https://localhost/whoami; echo; done
```

Qué debes observar en la salida JSON:

- `instanceId` alternando entre `app1`, `app2`, `app3`
- `hostname` cambiando según el contenedor

> Si solo ves una instancia, espera 15–30 segundos y repite (puede estar iniciando una de las apps o no pasar health check aún).

---

## 5) Verificar sesión distribuida con Redis

### 5.1 Crear valor de sesión con cookie persistida

```bash
curl -sk -c cookies.txt "https://localhost/session?value=sesion-ok"
```

### 5.2 Consultar múltiples veces usando misma cookie

```bash
for i in {1..8}; do curl -sk -b cookies.txt https://localhost/whoami; echo; done
```

Resultado esperado:

- `instanceId` debe ir cambiando (balanceo)
- `sessionValue` debe mantenerse en `sesion-ok`
- `sessionId` debe permanecer consistente para esa cookie

Esto demuestra que la sesión no depende de una sola instancia, sino de Redis.

---

## 6) Verificar tolerancia a fallos (failover)

Detén una instancia:

```bash
docker stop mockup-app2
```

Haz peticiones nuevamente:

```bash
for i in {1..10}; do curl -sk https://localhost/whoami; echo; done
```

Resultado esperado:

- Respuestas exitosas (sin `502 Bad Gateway`)
- Solo aparecen `app1` y `app3`

Revisa stats para confirmar `app2` DOWN:

- `http://localhost:8404/stats`

Vuelve a encender la instancia:

```bash
docker start mockup-app2
```

Espera unos segundos y verifica que vuelva a **UP** en stats.

---

## 7) Verificar logs de evidencia

```bash
docker compose logs --tail=100 haproxy
docker compose logs --tail=100 app1 app2 app3
```

Qué capturar para entrega:

- Logs mostrando peticiones distribuidas
- `docker compose ps`
- Pruebas de `/whoami`, `/session`, failover y redirect

---

## 8) Checklist final de aceptación

Marca cada punto:

- [ ] `docker compose up -d --build` levanta HAProxy + 3 apps + Redis + MySQL
- [ ] `http://localhost` redirige a `https://localhost`
- [ ] `https://localhost/whoami` alterna `instanceId` (Round Robin)
- [ ] Con `/session` + cookie, la sesión persiste aunque cambie de instancia
- [ ] Al detener una app, HAProxy sigue respondiendo con las otras (failover)
- [ ] Evidencias guardadas (capturas/terminal/logs)

---

## 9) Comandos útiles de recuperación

Si algo se rompe y quieres reiniciar limpio:

```bash
docker compose down -v
docker compose up -d --build
```

Si quieres reconstruir solo las apps:

```bash
docker compose build app1 app2 app3
docker compose up -d app1 app2 app3 haproxy
```

Si quieres limpiar cookie de prueba:

```bash
rm -f cookies.txt
```
