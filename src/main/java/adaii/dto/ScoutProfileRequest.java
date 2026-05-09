package adaii.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoutProfileRequest {

    @NotNull(message = "Age is required")
    @Min(value = 25, message = "Age must be at least 25")
    private Integer age;

    private String organizationName;

    private String region;
}