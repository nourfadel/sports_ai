package adaii.entity;

import jakarta.persistence.*;
import lombok.*;
import adaii.entity.enums.SessionStatus;
import adaii.entity.enums.SessionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "training_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_profile_id",nullable = false)
    private PlayerProfile playerProfile;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String notes;

    @PrePersist
    public void prPersist(){
        this.createdAt = LocalDateTime.now();
    }
}
