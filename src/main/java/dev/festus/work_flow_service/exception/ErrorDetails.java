package dev.festus.work_flow_service.exception;


import java.util.Date;

public record ErrorDetails(
         Date timeStamp,
         String message,
         String details,
         int statusCode) {

}