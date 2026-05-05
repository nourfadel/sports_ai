package adaii.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class SessionAnalysisResponse {

    private Long sessionId;
    private Long playerProfileId;

    private String riskLevel;
    private String fatigueLevel;

    private Double finalScore;
    private Double mfi;
    private Double mlProbability;

    private String recommendation;

    private Map<String, Double> components;

    private LocalDateTime analyzedAt;
}