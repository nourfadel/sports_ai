package adaii.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private TrainingSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_profile_id", nullable = false)
    private PlayerProfile playerProfile;

    @Column(nullable = false)
    private String riskLevel;

    @Column(nullable = false)
    private String fatigueLevel;

    @Column(nullable = false)
    private Double finalScore;

    @Column(nullable = false)
    private Double mfi;

    private Double mlProbability;

    @Column(nullable = false, length = 1000)
    private String recommendation;

    @Column(columnDefinition = "TEXT")
    private String componentsJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime analyzedAt;

    @PrePersist
    public void prePersist() {
        this.analyzedAt = LocalDateTime.now();
    }
}