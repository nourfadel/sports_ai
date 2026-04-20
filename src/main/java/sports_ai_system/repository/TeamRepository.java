package sports_ai_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sports_ai_system.entity.Team;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
    Optional<Team> findByName(String name);
}
