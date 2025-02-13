package kg.clientserviseoptima.common.exception;

import kg.clientserviseoptima.common.enums.ExceptionCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAlreadyExist extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public  ClientAlreadyExist(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}