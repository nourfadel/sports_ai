package adaii.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionSummaryResponse {

    private Long sessionId;

    private Double avgHeartRate;
    private Double maxHeartRate;

    private Double avgSpeed;
    private Double maxSpeed;

    private Double totalDistance;
    private Integer totalSprints;

    private Double avgPlayerLoad;
    private Double maxImpactForce;

    private Double avgBodyTemperature;

    private Integer readingsCount;
}