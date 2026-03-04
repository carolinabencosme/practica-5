# Práctica 5 - Balanceador de Carga con HAProxy + Sesiones Distribuidas

Esta implementación levanta un entorno de alta disponibilidad para la aplicación Spring Boot usando Docker Compose:

- **HAProxy** como balanceador con **Round Robin**
- **Terminación TLS/SSL** en HAProxy
- **Redirección HTTP (80) -> HTTPS (443)**
- **3 instancias de la app** (`app1`, `app2`, `app3`)
- **Redis** para sesiones distribuidas con Spring Session
- **MySQL** para persistencia de datos de la app

## Estructura

```text
.
├── docker-compose.yml
├── Dockerfile
├── haproxy/
│   ├── haproxy.cfg
│   └── certs/
│       ├── fullchain.pem
│       ├── privkey.pem
│       └── haproxy.pem
├── docs/
│   ├── REPORTE.md
│   └── PRESENTACION_GUIA.md
└── src/
```

## Despliegue rápido

```bash
docker compose up -d --build
```

Servicios expuestos:

- `https://localhost` (acceso principal)
- `http://localhost` (redirige a HTTPS)
- `http://localhost:8404/stats` (estadísticas de HAProxy)

> Nota: para desarrollo local se usa certificado **self-signed** (`haproxy/certs/haproxy.pem`).

## Endpoints de validación

- `GET /whoami` -> retorna `instanceId`, `hostname`, `serverPort`, `sessionId`, `sessionValue`
- `GET /session?value=demo123` -> guarda un valor en sesión distribuida (Redis)
- `GET /actuator/health` -> endpoint de health check para HAProxy

## Pruebas requeridas

### 1) Round Robin

```bash
for i in {1..10}; do curl -k https://localhost/whoami; echo; done
```

Debe alternar entre `app1`, `app2`, `app3`.

### 2) Sesión distribuida

1. Guardar sesión:
   ```bash
   curl -k -c cookies.txt "https://localhost/session?value=sesion-activa"
   ```
2. Consultar varias veces con la misma cookie:
   ```bash
   for i in {1..6}; do curl -k -b cookies.txt https://localhost/whoami; echo; done
   ```

Debe cambiar `instanceId`, pero mantener `sessionValue=sesion-activa` y el mismo estado de sesión.

### 3) Failover

```bash
docker stop mockup-app2
for i in {1..8}; do curl -k https://localhost/whoami; echo; done
```

Las respuestas deben continuar desde `app1` y `app3`, sin errores 502.

### 4) Redirect HTTP -> HTTPS

```bash
curl -I http://localhost
```

Debe retornar `301` con `Location: https://...`.

## Certificados en VM con Let's Encrypt (producción)

1. Apuntar DNS tipo A al IP público de la VM.
2. Instalar Certbot en la VM.
3. Emitir certificado:
   ```bash
   sudo certbot certonly --standalone -d app.mi-host-asignado
   ```
4. Generar PEM para HAProxy:
   ```bash
   sudo cat /etc/letsencrypt/live/app.mi-host-asignado/fullchain.pem \
            /etc/letsencrypt/live/app.mi-host-asignado/privkey.pem \
            > haproxy/certs/haproxy.pem
   ```
5. Reemplazar también `fullchain.pem` y `privkey.pem` en `haproxy/certs/`.
6. Reiniciar HAProxy:
   ```bash
   docker compose restart haproxy
   ```

## Evidencia sugerida para entrega

- `docker compose ps`
- `docker compose logs haproxy`
- prueba de `whoami` alternando instancias
- prueba de sesión distribuida con cookies
- prueba de failover (`docker stop mockup-app2`)
- prueba de redirección `80 -> 443`

Detalles para reporte y video en `docs/REPORTE.md` y `docs/PRESENTACION_GUIA.md`.
