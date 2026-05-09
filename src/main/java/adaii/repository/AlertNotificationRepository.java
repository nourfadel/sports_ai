package adaii.repository;

import adaii.entity.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertNotificationRepository extends JpaRepository<AlertNotification, Long> {

    List<AlertNotification> findBySessionIdOrderByCreatedAtDesc(Long sessionId);

    List<AlertNotification> findByPlayerProfileIdAndIsReadFalseOrderByCreatedAtDesc(Long playerProfileId);

    List<AlertNotification> findTop5BySessionTeamIdOrderByCreatedAtDesc(Long teamId);

    int countBySessionTeamIdAndIsReadFalse(Long teamId);
}