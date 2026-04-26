package ADAII.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ADAII.entity.TrainingSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Long> {
    Optional<TrainingSession> findByPlayerProfileIdOrderByCreatedAtDesc(Long playerId);
}
