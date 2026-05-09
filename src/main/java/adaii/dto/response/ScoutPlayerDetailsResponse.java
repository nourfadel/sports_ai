package adaii.dto.response;

import adaii.dto.LiveSensorDataResponse;
import adaii.dto.SessionAnalysisResponse;
import adaii.dto.TrainingSessionResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoutPlayerDetailsResponse {

    private Long playerProfileId;
    private Long userId;

    private String fullName;
    private String email;

    private Integer age;
    private String position;
    private String teamName;
    private String playerImageUrl;

    private Double heightCm;
    private Double weightKg;

    private Double overallScore;
    private Double potentialScore;

    private Boolean watchlisted;

    private TrainingSessionResponse latestSession;
    private LiveSensorDataResponse latestLiveData;
    private SessionAnalysisResponse latestAnalysis;
}