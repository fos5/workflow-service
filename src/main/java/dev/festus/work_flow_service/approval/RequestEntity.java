package dev.festus.work_flow_service.approval;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class RequestEntity {
    @Id
    private Long id;
    private String requestType;
    private String requestDescription;
    private String requesterEmail;
    private String status;
    private String approverName;
    private String approverId;
    private String approverComment;
    private String requestDate;
    private String approvalDate;
}
