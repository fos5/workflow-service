package dev.festus.work_flow_service.exception.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends  RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
