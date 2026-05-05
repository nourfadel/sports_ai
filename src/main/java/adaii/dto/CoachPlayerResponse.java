package adaii.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoachPlayerResponse {

    private Long playerProfileId;
    private Long userId;

    private String fullName;
    private String email;

    private String position;
    private Integer age;
    private Double   heightCm;
    private Double weightKg;

    private String teamName;
    private String playerImageUrl;
}