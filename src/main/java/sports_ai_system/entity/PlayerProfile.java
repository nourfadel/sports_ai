package sports_ai_system.entity;

import jakarta.persistence.*;
import lombok.*;
import sports_ai_system.entity.enums.Gender;
import sports_ai_system.entity.enums.PlayerPosition;

@Entity
@Table(name = "player_profiles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    private Integer age;
    private Double heightCm;
    private Double weightKg;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private PlayerPosition playerPosition;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String PlayerImageUrl;
}
