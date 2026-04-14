package sports_ai_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otps")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String otp;
    @Column(nullable = false)
    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
    private boolean used;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isExpired(){
        return expiryDate.isBefore(LocalDateTime.now());
    }

    public boolean canResendAfter(long seconds){
        return createdAt.plusSeconds(seconds).isBefore(LocalDateTime.now());
    }

}
