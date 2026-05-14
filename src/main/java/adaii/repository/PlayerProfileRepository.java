package adaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import adaii.entity.PlayerProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile,Long> {
    Optional<PlayerProfile> findByUserId(Long id);
    List<PlayerProfile> findByTeamId(Long teamId);
    boolean existsByUserId(Long userId);
}
