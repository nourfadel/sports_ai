package adaii.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoutProfileResponse {

    private Long id;
    private Long userId;

    private String fullName;
    private String email;

    private Integer age;
    private String organizationName;
    private String region;
}