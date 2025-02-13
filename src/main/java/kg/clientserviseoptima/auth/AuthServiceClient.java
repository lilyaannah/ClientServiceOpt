package kg.clientserviseoptima.auth;

import kg.clientserviseoptima.dto.request.AuthRequest;
import kg.clientserviseoptima.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    public String requestToken(String clientId, String password) {
        // Вместо запроса в реальный Auth Service просто возвращаем фейковый токен
        return "mock-session-id-123";
    }

//    private final RestTemplate restTemplate;
//
//    @Value("${auth.service.url}") // Укажи в application.properties -> auth.service.url=http://IP_АДРЕС_ДРУГА/auth
//    private String authServiceUrl;
//
//    public AuthServiceClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String requestToken(String clientId, String password) {
//        AuthRequest authRequest = new AuthRequest(clientId, password);
//
//        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
//                authServiceUrl + "/login", // Убрал лишний /auth
//                authRequest,
//                AuthResponse.class
//        );
//
//        if (response.getBody() == null) {
//            throw new RuntimeException("Ошибка авторизации: пустой ответ от AuthService");
//        }
//
//        return response.getBody().getSessionId();
//    }
}
