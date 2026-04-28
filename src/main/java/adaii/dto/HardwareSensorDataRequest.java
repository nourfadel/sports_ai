package adaii.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HardwareSensorDataRequest {
    private String deviceUuid;

    private Long sessionId;
    private Long playerProfileId;

    private LocalDateTime timestamp;

    private Double lat;
    private Double lng;

    @JsonProperty("speed_mps")
    private Double speedMps;

    @JsonProperty("distance_m")
    private Double distanceMeters;

    private Integer satellites;

    @JsonProperty("accel_x")
    private Double accelX;

    @JsonProperty("accel_y")
    private Double accelY;

    @JsonProperty("accel_z")
    private Double accelZ;

    @JsonProperty("sprints")
    private Integer sprints;

    @JsonProperty("player_load")
    private Double playerLoad;

    @JsonProperty("body_temp")
    private Double bodyTemp;

    @JsonProperty("heart_rate")
    private Double heartRate;

    private Double hrv;

    @JsonProperty("respiratory_rate")
    private Double respiratoryRate;
}
