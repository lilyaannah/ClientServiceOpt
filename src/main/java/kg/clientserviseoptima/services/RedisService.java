package kg.clientserviseoptima.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.clientserviseoptima.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveClientToCache(String clientId, Client client, long expirationTimeInSeconds) {
        redisTemplate.opsForValue().set(clientId, client, expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    public Client getClientFromCache(String clientId) {
        Object cachedData = redisTemplate.opsForValue().get(clientId);
        if (cachedData instanceof LinkedHashMap) {
            return objectMapper.convertValue(cachedData, Client.class); // Десериализация из LinkedHashMap
        }
        return (Client) cachedData;
    }

    public void deleteClientFromCache(String clientId) {
        redisTemplate.delete(clientId);
    }
}
