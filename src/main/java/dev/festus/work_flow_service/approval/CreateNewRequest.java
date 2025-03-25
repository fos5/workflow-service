package dev.festus.work_flow_service.approval;

public record CreateNewRequest(String requestType,
                               String requestDescription,
                               String requesterEmail
                               ){}
