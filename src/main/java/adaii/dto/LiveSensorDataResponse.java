package adaii.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LiveSensorDataResponse {

    private Long sessionId;
    private String deviceUuid;
    private LocalDateTime timestamp;

    private Double heartRate;
    private Double hrv;
    private Double respiratoryRate;

    private Double speed;
    private Double distance;

    private Double accelerationX;
    private Double accelerationY;
    private Double accelerationZ;

    private Double playerLoad;
    private Integer sprintCount;
    private Double impactForce;
    private Double bodyTemperature;

    private Double muscleFatigueIndex;
}