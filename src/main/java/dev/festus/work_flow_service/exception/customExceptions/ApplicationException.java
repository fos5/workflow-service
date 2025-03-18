package dev.festus.work_flow_service.exception.customExceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApplicationException extends Exception{

    public ApplicationException(String message) {
        super(message);
    }
    public ApplicationException( int statusCode, String message){
    }
}