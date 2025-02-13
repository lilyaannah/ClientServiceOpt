package kg.clientserviseoptima.controlles;

import kg.clientserviseoptima.dto.request.ClientDto;
import kg.clientserviseoptima.dto.response.ClientResponse;
import kg.clientserviseoptima.services.ClientService;
import kg.clientserviseoptima.services.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/client/v1")
public class ClientController {
    private final ClientService clientService;
    private final RedisService redisService;

    public ClientController(ClientService clientService, RedisService redisService) {
        this.clientService = clientService;
        this.redisService = redisService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> add(@RequestBody ClientDto clientDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.saveClient(clientDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ClientResponse> loginClient(@RequestParam String phoneNumber, @RequestParam String password) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.loginClient(phoneNumber, password));
    }

    // Обновление данных клиента
    @PutMapping()
    public ResponseEntity<ClientResponse> updateClient( @RequestBody ClientDto clientDto) {
        ClientResponse updatedClientResponse = clientService.updateClientData(clientDto);
        if (updatedClientResponse != null) {
            return new ResponseEntity<>(updatedClientResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Получение данных клиента по номеру телефона
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<ClientResponse> getClientData(@PathVariable String phoneNumber) {
        ClientResponse clientResponse = clientService.getClientData(phoneNumber);
        if (clientResponse != null) {
            return new ResponseEntity<>(clientResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{phoneNumber}")
    public ResponseEntity<Map<String, String>> deleteClientData(@PathVariable String phoneNumber) {
        clientService.deleteClient(phoneNumber);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Клиент с номером " + phoneNumber + " успешно удалён");

        return ResponseEntity.ok(response);
    }
}
