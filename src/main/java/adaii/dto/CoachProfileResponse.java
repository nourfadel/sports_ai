package adaii.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoachProfileResponse {

    private Long id;
    private Long userId;

    private String fullName;
    private String email;

    private String teamName;
    private String specialization;
    private Integer yearsOfExperience;
    private String licenseNumber;
}