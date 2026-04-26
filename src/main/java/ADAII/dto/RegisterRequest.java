package ADAII.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ADAII.entity.enums.Role;

@Data
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


    @Email(message = "Invalid email Format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min =8 , max = 12,message = "Password must be at least 8 chars and at most 12 chars")
    private String password;

    @NotNull
    private Role role;
}
