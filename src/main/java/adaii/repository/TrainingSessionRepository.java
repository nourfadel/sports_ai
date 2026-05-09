package adaii.repository;

import adaii.entity.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import adaii.entity.TrainingSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Long> {
    List<TrainingSession> findByPlayerProfileIdOrderByCreatedAtDesc(Long playerId);

    Optional<TrainingSession> findByPlayerProfileIdAndStatus(Long playerProfileId,SessionStatus status);
    List<TrainingSession> findByTeamIdAndStatus(Long teamId, SessionStatus status);
    Optional<TrainingSession> findTopByPlayerProfileIdOrderByCreatedAtDesc(Long playerProfileId);
    Optional<TrainingSession> findFirstByPlayerProfileIdAndStatusOrderByStartedAtDesc(
            Long playerProfileId,
            SessionStatus status
    );
}
