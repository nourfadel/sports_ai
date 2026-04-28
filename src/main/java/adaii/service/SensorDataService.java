package adaii.service;

import adaii.dto.HardwareSensorDataRequest;
import adaii.entity.DeviceAssignment;
import adaii.entity.PlayerProfile;
import adaii.entity.SensorData;
import adaii.entity.TrainingSession;
import adaii.entity.enums.SessionStatus;
import adaii.exception.InvalidSessionStateException;
import adaii.exception.PlayerProfileNotFoundException;
import adaii.exception.SessionNotFoundException;
import adaii.repository.DeviceAssignmentRepository;
import adaii.repository.PlayerProfileRepository;
import adaii.repository.SensorDataRepository;
import adaii.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceAssignmentRepository deviceAssignmentRepository;

    public void ingest(HardwareSensorDataRequest request){

        DeviceAssignment assignment = deviceAssignmentRepository
                .findByDeviceUuidAndActiveTrue(request.getDeviceUuid())
                .orElseThrow(() -> new RuntimeException("Device is not assigned to any active player"));

        PlayerProfile profile = assignment.getPlayerProfile();

        TrainingSession session = trainingSessionRepository.findByPlayerProfileIdAndStatus(profile.getId(),
                        SessionStatus.ACTIVE
                        )
                .orElseThrow(() -> new InvalidSessionStateException("No active session found for this player"));

        SensorData sensorData = SensorData.builder()
                .deviceUuid(request.getDeviceUuid())
                .session(session)
                .playerProfile(profile)
                .timestamp(request.getTimestamp())
                .latitude(request.getLat())
                .longitude(request.getLng())
                .satellites(request.getSatellites())
                .speed(request.getSpeedMps())
                .distance(request.getDistanceMeters())
                .accelerationX(request.getAccelX())
                .accelerationY(request.getAccelY())
                .accelerationZ(request.getAccelZ())
                .sprintCount(request.getSprints())
                .playerLoad(request.getPlayerLoad())
                .bodyTemperature(request.getBodyTemp())
                .heartRate(request.getHeartRate())
                .hrv(request.getHrv())
                .respiratoryRate(request.getRespiratoryRate())
                .build();


        sensorDataRepository.save(sensorData);
    }

}
