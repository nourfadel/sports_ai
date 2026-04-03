package sports_ai_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sports_ai_system.entity.PasswordResetOtp;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp,Long> {

    // function to find newestOtp
    Optional<PasswordResetOtp> findTopByEmailOrderByExpiryDateDesc(String email);

    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
}
