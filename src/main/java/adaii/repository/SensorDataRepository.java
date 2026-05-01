package adaii.repository;

import adaii.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData,Long> {
    Optional<SensorData> findTopBySessionIdAndPlayerProfileIdOrderByTimestampDesc(
            Long sessionId,Long playerProfileId);

    Optional<SensorData> findTopBySessionIdOrderByTimestampDesc(Long sessionId);

    List<SensorData> findBySessionIdOrderByTimestampAsc(Long sessionId);
}
