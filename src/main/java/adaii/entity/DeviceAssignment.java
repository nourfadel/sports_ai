package adaii.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String deviceUuid;

    @OneToOne(optional = false)
    @JoinColumn(name = "player_profile_id",nullable = false)
    private PlayerProfile playerProfile;

    @Column(nullable = false,updatable = false)
    private Boolean active;

    @Column(nullable = false,updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    public void prePersist(){
        this.assignedAt = LocalDateTime.now();
        if (this.active == null){
            this.active = true;
        }
    }

}
