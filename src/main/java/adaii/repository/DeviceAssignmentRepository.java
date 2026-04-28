package adaii.repository;

import adaii.entity.DeviceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceAssignmentRepository extends JpaRepository<DeviceAssignment,Long> {

    Optional<DeviceAssignment> findByDeviceUuidAndActiveTrue(String deviceUuid);
}
