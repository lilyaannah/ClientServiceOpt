package kg.clientserviseoptima.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kg.clientserviseoptima.common.enums.ClientStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID clientId; // UUID для Redis и общения между сервисами

    private String clientName;
    private String clientLastname;
    private String clientPhoneNumber;

    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @JsonIgnore
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTime;

}
