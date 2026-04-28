package adaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import adaii.entity.PasswordResetOtp;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp,Long> {

    // function to find newestOtp
    Optional<PasswordResetOtp> findTopByEmailOrderByExpiryDateDesc(String email);

    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
}
