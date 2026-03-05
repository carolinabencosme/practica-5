package org.example.primera_practica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Fuerza el uso de Redis como almacén de sesiones HTTP.
 * Sin esta anotación, Spring Session puede no registrar el SessionRepository
 * y las instancias usan sesión en memoria, por lo que Redis queda vacío
 * y cada app emite su propia JSESSIONID (set-cookie) al no encontrar la sesión.
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
}
