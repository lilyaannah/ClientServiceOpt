package kg.clientserviseoptima.services;

import kg.clientserviseoptima.auth.AuthServiceClient;
import kg.clientserviseoptima.common.exception.ClientAlreadyExist;
import kg.clientserviseoptima.common.exception.ClientNotFoundException;
import kg.clientserviseoptima.common.exception.UnauthorizedException;
import kg.clientserviseoptima.dto.request.ClientDto;
import kg.clientserviseoptima.dto.response.ClientResponse;
import kg.clientserviseoptima.models.Client;
import kg.clientserviseoptima.rabbitmq.RabbitMQProducer;
import kg.clientserviseoptima.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static kg.clientserviseoptima.common.enums.ClientStatus.*;
import static kg.clientserviseoptima.common.enums.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final RabbitMQProducer rabbitMQProducer;
    private final AuthServiceClient authServiceClient;

    public ClientResponse saveClient(ClientDto clientDto) {
        if (clientRepository.findByClientPhoneNumberAndStatusNotDeleted(clientDto.getClientPhoneNumber()).isPresent()) {
            throw new ClientAlreadyExist(FOUND);
        }

        UUID clientId = UUID.randomUUID();

        Client client = clientRepository.save(
                Client.builder()
                        .clientId(clientId)
                        .clientName(clientDto.getClientName())
                        .clientLastname(clientDto.getClientLastname())
                        .clientPhoneNumber(clientDto.getClientPhoneNumber())
                        .clientStatus(CREATED)
                        .password(passwordEncoder.encode(clientDto.getPassword()))
                        .lastActiveTime(LocalDateTime.now())
                        .build());

        redisService.saveClientToCache(clientId.toString(), client, 3600);

        // Запрос в Auth Service (логин после регистрации)
        String sessionId = authServiceClient.requestToken(clientId.toString(), clientDto.getPassword());

        return ClientResponse.builder()
                .clientId(clientId)
                .clientName(client.getClientName())
                .clientLastname(client.getClientLastname())
                .clientPhoneNumber(client.getClientPhoneNumber())
                .sessionId(sessionId) // Отдаем sessionId вместо токена
                .build();
    }

    public ClientResponse loginClient(String phoneNumber, String password) {
        Optional<Client> optionalClient = clientRepository.findByClientPhoneNumber(phoneNumber);

        if (optionalClient.isEmpty()) {
            throw new ClientNotFoundException(NOT_FOUND); // Если клиент не найден
        }

        Client client = optionalClient.get();

        // Проверяем правильность пароля
        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new UnauthorizedException(INCORRECT); // Выбросить исключение, если пароли не совпадают
        }

        // Если пароль верный, получаем sessionId из AuthService
        String sessionId = authServiceClient.requestToken(client.getClientId().toString(), password);

        return ClientResponse.builder()
                .clientId(client.getClientId())
                .clientName(client.getClientName())
                .clientLastname(client.getClientLastname())
                .clientPhoneNumber(client.getClientPhoneNumber())
                .sessionId(sessionId) // Токен сессии
                .build();
    }



    public ClientResponse updateClientData(ClientDto clientDto) {
        Optional<Client> optionalClient = clientRepository.findByClientPhoneNumberAndStatusNotDeleted(clientDto.getClientPhoneNumber());

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            client.setClientName(clientDto.getClientName());
            client.setClientLastname(clientDto.getClientLastname());
            client.setClientPhoneNumber(clientDto.getClientPhoneNumber());
            client.setLastActiveTime(LocalDateTime.now());
            client.setClientStatus(UPDATED);

            clientRepository.save(client);
            redisService.saveClientToCache(client.getClientPhoneNumber(), client, 3600);

            ClientResponse event = ClientResponse.builder()
                    .clientPhoneNumber(client.getClientPhoneNumber())
                    .clientName(client.getClientName())
                    .clientLastname(client.getClientLastname())
                    .build();
            rabbitMQProducer.sendEvent("my-exchange", event);

            return ClientResponse.builder()
                    .clientName(client.getClientName())
                    .clientLastname(client.getClientLastname())
                    .clientPhoneNumber(client.getClientPhoneNumber())
                    .build();
        }

        throw new ClientNotFoundException(NOT_FOUND);
    }

    public ClientResponse getClientData(String phoneNumber) {
        Client cachedClient = redisService.getClientFromCache(phoneNumber);
        if (cachedClient != null) {
            LocalDateTime now = LocalDateTime.now();
            cachedClient.setLastActiveTime(now);
            redisService.saveClientToCache(phoneNumber, cachedClient, 3600);
            clientRepository.updateLastActiveTime(phoneNumber, now);
            return ClientResponse.builder()
                    .clientName(cachedClient.getClientName())
                    .clientLastname(cachedClient.getClientLastname())
                    .clientPhoneNumber(cachedClient.getClientPhoneNumber())
                    .build();
        }

        Optional<Client> optionalClient = clientRepository.findByClientPhoneNumberAndStatusNotDeleted(phoneNumber);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            LocalDateTime now = LocalDateTime.now();
            clientRepository.updateLastActiveTime(phoneNumber, now); // Обновляем БД напрямую
            client.setLastActiveTime(now);
            redisService.saveClientToCache(phoneNumber, client, 3600);

            return ClientResponse.builder()
                    .clientName(client.getClientName())
                    .clientLastname(client.getClientLastname())
                    .clientPhoneNumber(client.getClientPhoneNumber())
                    .build();
        }

        throw new ClientNotFoundException(NOT_FOUND);
    }



    public void deleteClient(String phoneNumber) {
        Optional<Client> optionalClient = clientRepository.findByClientPhoneNumberAndStatusNotDeleted(phoneNumber);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setClientStatus(DELETED);
            client.setLastActiveTime(LocalDateTime.now());
            clientRepository.save(client);
            redisService.deleteClientFromCache(phoneNumber);
        } else {
            throw new ClientNotFoundException(NOT_FOUND);
        }
    }
}
