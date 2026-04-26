package ADAII.service;

import ADAII.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ADAII.entity.PasswordResetOtp;
import ADAII.entity.User;
import ADAII.exception.*;
import ADAII.repository.PasswordResetOtpRepository;
import ADAII.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private String generateOtp(){
        String otp = String.valueOf((int)( Math.random() * 900000) +100000);
        return otp;
    }

    public String forgotPassword(String email){
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        String otp = generateOtp();

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setUsed(false);
        resetOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5));
//        resetOtp.setCreatedAt(LocalDateTime.now());

        passwordResetOtpRepository.save(resetOtp);
        emailService.sendOtpEmail(email,otp);

        return "OTP Sent";
    }



    public String resetPassword(String email,String otp,String newPassword){

        PasswordResetOtp resetOtp = passwordResetOtpRepository
                .findByEmailAndOtp(email,otp)
                .orElseThrow(() -> new RuntimeException("OTP not found!"));

        if (resetOtp.isUsed()){
            throw new OtpAlreadyUsedException("OTP already used");
        }

        if (!resetOtp.getOtp().equals(otp)){
            throw new InvalidOtpException("Invalid OTP");
        }

        if (resetOtp.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new OtpExpiredException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetOtp.setUsed(true);
        passwordResetOtpRepository.save(resetOtp);

        return "Password reset Successfully";
    }

    public String resendOtp(String email){
        PasswordResetOtp lastOtp = passwordResetOtpRepository
                .findTopByEmailOrderByExpiryDateDesc(email)
                .orElseThrow(()-> new NoPreviousOtpFoundException("No previous OTP request found"));

        long seconds = 60;
        if (!lastOtp.canResendAfter(seconds)){
            throw new OtpResendTooSoonException("Please wait "+ seconds + " before requesting another OTP");
        }

        lastOtp.setUsed(true);
        passwordResetOtpRepository.save(lastOtp);

        return forgotPassword(email);
    }

}
