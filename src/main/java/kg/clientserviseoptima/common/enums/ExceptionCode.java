package kg.clientserviseoptima.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    INCORRECT(1, "Неверный пароль", HttpStatus.UNAUTHORIZED),
    NOT_FOUND(2, "Не найдено", HttpStatus.NOT_FOUND),
    BAD_REQUEST(3, "404 bad request", HttpStatus.BAD_REQUEST),
    FOUND(4, "Клиент существует", HttpStatus.FOUND);

    private final int code;
    private final String message;
    private final HttpStatus status;
}