package org.example.primera_practica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Fuerza el uso de Redis como almacén de sesiones HTTP y fija el namespace esperado
 * para que las claves aparezcan como mockup:sessions* en Redis.
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "mockup:sessions")
public class SessionConfig {
}
