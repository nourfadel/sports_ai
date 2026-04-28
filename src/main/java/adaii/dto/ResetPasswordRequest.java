package adaii.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "OTP is required!")
    private String otp;

    @NotBlank(message = "New Password is required!")
    private String newPassword;
}
