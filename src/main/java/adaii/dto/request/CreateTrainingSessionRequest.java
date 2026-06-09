package adaii.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import adaii.entity.enums.SessionType;

import java.time.LocalDateTime;

@Data
public class CreateTrainingSessionRequest {

    @NotNull(message = "Session type is required")
    private SessionType sessionType;

    private LocalDateTime scheduledAt;
    private String notes;
}
