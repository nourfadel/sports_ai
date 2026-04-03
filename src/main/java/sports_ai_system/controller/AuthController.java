package sports_ai_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sports_ai_system.dto.AuthResponse;
import sports_ai_system.dto.LoginRequest;
import sports_ai_system.dto.RegisterRequest;
import sports_ai_system.service.AuthService;
import sports_ai_system.service.OtpService;

import java.util.Map;

@RestController
@RequestMapping("/api/sports/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        return ResponseEntity.ok(
                otpService.forgotPassword(email)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> request){
        return ResponseEntity.ok(
                otpService.resetPassword(
                        request.get("email"),
                        request.get("otp"),
                        request.get("newPassword")
                )
        );
    }
}
