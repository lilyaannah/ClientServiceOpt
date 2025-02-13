package kg.clientserviseoptima.dto.response;

import kg.clientserviseoptima.common.enums.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ClientResponse implements Serializable {
    private UUID clientId;
    private String clientName;
    private String clientLastname;
    private String clientPhoneNumber;
    private String sessionId;
}
