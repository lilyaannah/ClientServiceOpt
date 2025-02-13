package kg.clientserviseoptima.dto.response;

import kg.clientserviseoptima.common.enums.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private ExceptionCode code;
    private String message;
}