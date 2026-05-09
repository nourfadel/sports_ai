package adaii.service;

import adaii.dto.HardwareSensorDataRequest;
import adaii.dto.LiveSensorDataResponse;
import adaii.dto.SensorDataResponse;
import adaii.dto.SessionSummaryResponse;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceAssignmentRepository deviceAssignmentRepository;
    private final AlertService alertService;

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


        SensorData savedData = sensorDataRepository.save(sensorData);
        alertService.checkAndCreateAlerts(savedData);
    }

    // get live data
    public LiveSensorDataResponse getLiveData(Long sessionId) {
        SensorData sensorData = sensorDataRepository.findTopBySessionIdOrderByTimestampDesc(sessionId)
                .orElseThrow(() -> new RuntimeException("No live data found for this session"));

        return mapToLiveResponse(sensorData);
    }

    public List<SensorDataResponse> getSessionData(Long sessionId){

        List<SensorData> dataResponseList = sensorDataRepository.findBySessionIdOrderByTimestampAsc(sessionId);

        return dataResponseList.stream()
                .map(data -> SensorDataResponse.builder()
                        .timeStamp(data.getTimestamp())
                        .heartRate(data.getHeartRate())
                        .speed(data.getSpeed())
                        .distance(data.getDistance())
                        .playerLoad(data.getPlayerLoad())
                        .build()
                ).toList();

    }

    // add method to get session summary
    // do not forget to add aggregation function to calculate this in future *******************
    public SessionSummaryResponse getSessionSummary(Long sessionId) {

        List<SensorData> dataList = sensorDataRepository.findBySessionIdOrderByTimestampAsc(sessionId);

        if (dataList.isEmpty()) {
            throw new SessionNotFoundException("No sensor data found for this session");
        }

        Double avgHeartRate = dataList.stream()
                .filter(d -> d.getHeartRate() != null)
                .mapToDouble(SensorData::getHeartRate)
                .average()
                .orElse(0);

        Double maxHeartRate = dataList.stream()
                .filter(d -> d.getHeartRate() != null)
                .mapToDouble(SensorData::getHeartRate)
                .max()
                .orElse(0);

        Double avgSpeed = dataList.stream()
                .filter(d -> d.getSpeed() != null)
                .mapToDouble(SensorData::getSpeed)
                .average()
                .orElse(0);

        Double maxSpeed = dataList.stream()
                .filter(d -> d.getSpeed() != null)
                .mapToDouble(SensorData::getSpeed)
                .max()
                .orElse(0);

        Double totalDistance = dataList.stream()
                .filter(d -> d.getDistance() != null)
                .mapToDouble(SensorData::getDistance)
                .max()
                .orElse(0);

        Integer totalSprints = dataList.stream()
                .filter(d -> d.getSprintCount() != null)
                .mapToInt(SensorData::getSprintCount)
                .max()
                .orElse(0);

        Double avgPlayerLoad = dataList.stream()
                .filter(d -> d.getPlayerLoad() != null)
                .mapToDouble(SensorData::getPlayerLoad)
                .average()
                .orElse(0);

        Double maxImpactForce = dataList.stream()
                .filter(d -> d.getImpactForce() != null)
                .mapToDouble(SensorData::getImpactForce)
                .max()
                .orElse(0);

        Double avgBodyTemperature = dataList.stream()
                .filter(d -> d.getBodyTemperature() != null)
                .mapToDouble(SensorData::getBodyTemperature)
                .average()
                .orElse(0);

        return SessionSummaryResponse.builder()
                .sessionId(sessionId)
                .avgHeartRate(avgHeartRate)
                .maxHeartRate(maxHeartRate)
                .avgSpeed(avgSpeed)
                .maxSpeed(maxSpeed)
                .totalDistance(totalDistance)
                .totalSprints(totalSprints)
                .avgPlayerLoad(avgPlayerLoad)
                .maxImpactForce(maxImpactForce)
                .avgBodyTemperature(avgBodyTemperature)
                .readingsCount(dataList.size())
                .build();
    }

    public Optional<LiveSensorDataResponse> getLatestLiveDataIfExists(Long sessionId) {
        return sensorDataRepository.findTopBySessionIdOrderByTimestampDesc(sessionId)
                .map(this::mapToLiveResponse);
    }

    private LiveSensorDataResponse mapToLiveResponse(SensorData sensorData) {
        return LiveSensorDataResponse.builder()
                .sessionId(sensorData.getSession().getId())
                .deviceUuid(sensorData.getDeviceUuid())
                .timestamp(sensorData.getTimestamp())
                .heartRate(sensorData.getHeartRate())
                .hrv(sensorData.getHrv())
                .respiratoryRate(sensorData.getRespiratoryRate())
                .speed(sensorData.getSpeed())
                .distance(sensorData.getDistance())
                .accelerationX(sensorData.getAccelerationX())
                .accelerationY(sensorData.getAccelerationY())
                .accelerationZ(sensorData.getAccelerationZ())
                .playerLoad(sensorData.getPlayerLoad())
                .sprintCount(sensorData.getSprintCount())
                .impactForce(sensorData.getImpactForce())
                .bodyTemperature(sensorData.getBodyTemperature())
                .muscleFatigueIndex(sensorData.getMuscleFatigueIndex())
                .build();
    }
}



















