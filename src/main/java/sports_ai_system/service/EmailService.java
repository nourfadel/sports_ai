package sports_ai_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String toEmail,String otp){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText("""
            Hello,
            
            Your OTP for password reset is: %s
            
            This code will expire in 10 minutes.
            
            If you didn't request this, ignore this email.
            
            Thanks,
            Sports AI Team
            """.formatted(otp)
        );
        javaMailSender.send(mailMessage);
    }

}
