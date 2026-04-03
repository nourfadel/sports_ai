package sports_ai_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sports_ai_system.entity.PasswordResetOtp;
import sports_ai_system.entity.User;
import sports_ai_system.repository.PasswordResetOtpRepository;
import sports_ai_system.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public String generateOtp(){
        String otp = String.valueOf((int)( Math.random() * 900000) +100000);
        return otp;
    }

    public String forgotPassword(String email){
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        String otp = generateOtp();

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setUsed(false);
        resetOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        passwordResetOtpRepository.save(resetOtp);
        emailService.sendOtpEmail(email,otp);

        return "OTP Sent";
    }



    public String resetPassword(String email,String otp,String newPassword){

        PasswordResetOtp resetOtp = passwordResetOtpRepository
                .findByEmailAndOtp(email,otp)
                .orElseThrow(() -> new RuntimeException("OTP not found!"));

        if (resetOtp.isUsed()){
            throw new RuntimeException("OTP already used");
        }

        if (!resetOtp.getOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if (resetOtp.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetOtp.setUsed(true);
        passwordResetOtpRepository.save(resetOtp);

        return "Password reset Successfully";
    }

}
