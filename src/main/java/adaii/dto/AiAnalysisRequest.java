package adaii.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiAnalysisRequest {

    private Long sessionId;
    private Long playerProfileId;

    private List<Double> heartRates;
    private List<Double> hrvs;
    private List<Double> speeds;
    private List<Double> distances;
    private List<Double> playerLoads;
    private List<Integer> sprintCounts;
    private List<Double> impactForces;
    private List<Double> bodyTemperatures;
    private List<Double> respiratoryRates;
}