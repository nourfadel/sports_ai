package adaii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoachProfileRequest {

    @NotBlank(message = "Team name is required")
    private String teamName;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Years of experience is required")
    private Integer yearsOfExperience;

    private String licenseNumber;
}