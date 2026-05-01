package adaii.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SensorDataResponse {
    private LocalDateTime timeStamp;
    private Double heartRate;
    private Double speed;
    private Double distance;
    private Double playerLoad;
}
