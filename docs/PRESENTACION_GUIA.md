# GUÍA DE PRESENTACIÓN (10 minutos)

## Minuto 0-1: Introducción
- Problema: alta disponibilidad + mantener sesión en arquitectura con múltiples réplicas.
- Objetivo de la práctica.

## Minuto 1-3: Arquitectura
- Mostrar diagrama: Cliente -> HAProxy -> 3 apps -> Redis/MySQL.
- Explicar por qué Redis evita pérdida de sesión al cambiar de instancia.

## Minuto 3-6: Demo en vivo
1. `docker compose up -d --build`
2. Mostrar `docker compose ps`
3. Probar `https://localhost/whoami` varias veces (Round Robin)
4. Probar `/session?value=...` + `/whoami` con cookies (sesión distribuida)

## Minuto 6-8: Tolerancia a fallos y seguridad
- `docker stop mockup-app2`
- Repetir `whoami` y demostrar continuidad.
- Mostrar redirect `http://localhost` -> `https://localhost`.

## Minuto 8-9: Configuración TLS
- Modo local self-signed.
- Flujo producción con Let's Encrypt + `haproxy.pem`.

## Minuto 9-10: Cierre
- Checklist de cumplimiento.
- Lecciones aprendidas.

## Checklist para video
- [ ] `docker compose up -d --build`
- [ ] `docker compose ps`
- [ ] RR demostrado con `/whoami`
- [ ] Sesión distribuida demostrada
- [ ] Failover con una instancia caída
- [ ] Redirect 80->443
- [ ] Evidencia de logs de HAProxy/app
