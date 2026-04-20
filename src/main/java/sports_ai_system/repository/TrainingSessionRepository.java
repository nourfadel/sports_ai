package sports_ai_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sports_ai_system.entity.TrainingSession;

import java.util.Optional;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Long> {
    Optional<TrainingSession> findByPlayerProfileIdOrderByCreatedAtDesc(Long playerId);
}
