package adaii.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoutPlayerResponse {

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
}