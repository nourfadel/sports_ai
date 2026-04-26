package ADAII.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ADAII.entity.enums.Gender;
import ADAII.entity.enums.PlayerPosition;

@Data
public class PlayerProfileRequest {

    @NotNull(message = "Age is required")
    private Integer age;

    @NotNull(message = "Height is required")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    private Double weightKg;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Primary position is required")
    private PlayerPosition primaryPosition;

    @NotBlank(message = "Team name is required")
    private String teamName;

}
