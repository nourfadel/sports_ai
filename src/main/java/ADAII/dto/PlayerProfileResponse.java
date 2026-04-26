package ADAII.dto;

import lombok.Builder;
import lombok.Data;
import ADAII.entity.enums.Gender;
import ADAII.entity.enums.PlayerPosition;

@Data
@Builder
public class PlayerProfileResponse {

    private Long id;

    private String fullName;
    private String email;

    private Integer age;
    private Double heightCm;
    private Double weightKg;

    private Gender gender;
    private PlayerPosition primaryPosition;

    private String teamName;
    private String profileImageUrl;

}
