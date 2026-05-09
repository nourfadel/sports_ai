package adaii.repository;

import adaii.entity.SessionAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionAnalysisRepository extends JpaRepository<SessionAnalysis, Long> {

    Optional<SessionAnalysis> findBySessionId(Long sessionId);
    int countBySessionTeamIdAndRiskLevel(Long teamId, String riskLevel);
}