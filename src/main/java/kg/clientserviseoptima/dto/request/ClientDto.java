package kg.clientserviseoptima.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private String clientName;
    private String clientLastname;
    private String clientPhoneNumber;
    private String password;
}
