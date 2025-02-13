package kg.clientserviseoptima.common.exception;

import kg.clientserviseoptima.common.enums.ExceptionCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public  UnauthorizedException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}