package dev.festus.work_flow_service.approval;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
public class ApprovalEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String requestId;
    private String approverId;
    private String approverName;
    private String status;
    private String comment;
}
