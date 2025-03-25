package dev.festus.work_flow_service;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(String message, boolean success, Object data, List<String> errors) {

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(message, true, data, null);
    }

    public static ApiResponse failure(String message, List<String> errors) {
        return new ApiResponse(message, false, null, errors);
    }
}


