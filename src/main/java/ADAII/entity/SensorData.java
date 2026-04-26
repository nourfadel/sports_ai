package ADAII.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data",
    indexes = {
            @Index(name = "idx_sensor_player", columnList = "player_profile_id"),
            @Index(name = "idx_sensor_session", columnList = "session_id"),
            @Index(name = "idx_sensor_device", columnList = "device_uuid"),
            @Index(name = "idx_sensor_timestamp", columnList = "timestamp")    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceUuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_profile_id", nullable = false)
    private PlayerProfile playerProfile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private TrainingSession session;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Double latitude;
    private Double longitude;
    private Integer satellites;

    private Double heartRate;
    private Double hrv;
    private Double respiratoryRate;

    private Double speed;
    private Double distance;

    private Double accelerationX;
    private Double accelerationY;
    private Double accelerationZ;

    private Double playerLoad;
    private Integer sprintCount;
    private Double impactForce;
    private Double bodyTemperature;

    private Double muscleFatigueIndex;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        if (this.timestamp == null){
            this.timestamp = LocalDateTime.now();
        }
    }

}
