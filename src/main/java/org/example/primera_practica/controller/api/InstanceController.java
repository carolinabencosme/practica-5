package org.example.primera_practica.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class InstanceController {

    @Value("${INSTANCE_ID:app-unknown}")
    private String instanceId;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping("/whoami")
    public ResponseEntity<Map<String, String>> whoami(HttpServletRequest request, HttpSession session)
            throws UnknownHostException {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("instanceId", instanceId);
        response.put("hostname", InetAddress.getLocalHost().getHostName());
        response.put("serverPort", serverPort);
        response.put("sessionId", session.getId());
        response.put("sessionValue", String.valueOf(session.getAttribute("demoValue")));
        response.put("requestUri", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, String>> setSessionValue(
            @RequestParam(name = "value", required = false, defaultValue = "valor-demo") String value,
            HttpSession session) {
        session.setAttribute("demoValue", value);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Session attribute updated");
        response.put("sessionId", session.getId());
        response.put("storedValue", value);
        response.put("instanceId", instanceId);
        return ResponseEntity.ok(response);
    }
}
