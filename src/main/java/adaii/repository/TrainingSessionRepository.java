package adaii.repository;

import adaii.entity.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import adaii.entity.TrainingSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Long> {
    Optional<TrainingSession> findByPlayerProfileIdOrderByCreatedAtDesc(Long playerId);

    Optional<TrainingSession> findByPlayerProfileIdAndStatus(Long playerProfileId,SessionStatus status);
}
