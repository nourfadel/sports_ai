package sports_ai_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sports_ai_system.enums.Role;

@Data
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min =8 , max = 12)
    @NotBlank
    private String password;

    @NotBlank
    private Role role;
}
