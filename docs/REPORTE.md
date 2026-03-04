# REPORTE - Práctica Balanceador de Carga

## 1. Objetivo
Implementar alta disponibilidad con Docker Compose usando HAProxy (Round Robin + TLS termination), tres instancias Spring Boot y sesiones distribuidas con Redis.

## 2. Arquitectura
- Cliente -> HTTPS -> HAProxy
- HAProxy -> HTTP interno -> app1/app2/app3 (Round Robin)
- app1/app2/app3 -> Redis (Spring Session)
- app1/app2/app3 -> MySQL (persistencia)

## 3. Componentes implementados
- `docker-compose.yml`: orquestación de `haproxy`, `app1`, `app2`, `app3`, `redis`, `db`
- `haproxy/haproxy.cfg`: redirect 80->443, TLS, health checks, round robin
- `haproxy/certs/*`: certificados para entorno local
- `src/main/.../InstanceController.java`: endpoints `/whoami` y `/session`
- `application.properties`: configuración Spring Session con Redis + actuator health
- `SecurityConfig`: permisos para endpoints de verificación

## 4. Pruebas ejecutadas (guía)
1. **Levantar entorno**: `docker compose up -d --build`
2. **Round Robin**: `curl -k https://localhost/whoami` repetido
3. **Sesión distribuida**: usar `cookies.txt` con `/session` y `/whoami`
4. **Failover**: detener `app2` y verificar continuidad
5. **Redirect**: `curl -I http://localhost` debe retornar 301 a HTTPS

## 5. Resultados esperados
- Alternancia de `instanceId` entre app1/app2/app3
- Persistencia de `sessionValue` aunque cambie la instancia
- Respuesta estable al detener una instancia
- HTTPS activo y redirección desde HTTP

## 6. Conclusiones
La solución cumple requisitos de balanceo, tolerancia a fallos, terminación SSL/TLS y sesiones distribuidas en un entorno reproducible con Docker Compose.
