package ADAII.dto;

import lombok.Builder;
import lombok.Data;
import ADAII.entity.enums.SessionStatus;
import ADAII.entity.enums.SessionType;

import java.time.LocalDateTime;

@Data
@Builder
public class TrainingSessionResponse {

    private Long id;
    private SessionType sessionType;
    private SessionStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String notes;
    private String teamName;

}
