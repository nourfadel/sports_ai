package sports_ai_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sports_ai_system.entity.PlayerProfile;

import java.util.Optional;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile,Long> {
    Optional<PlayerProfile> findByUserId(Long id);
}
