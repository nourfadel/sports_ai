package adaii.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AiAnalysisResponse {

    private Long sessionId;
    private Long playerProfileId;

    private String riskLevel;
    private String fatigueLevel;

    private Double finalScore;
    private Double mfi;
    private Double mlProbability;

    private String recommendation;

    private Map<String, Double> components;
}