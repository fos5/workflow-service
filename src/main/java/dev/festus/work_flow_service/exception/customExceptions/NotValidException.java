package dev.festus.work_flow_service.exception.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotValidException(String message) {
        super(message);
    }
}