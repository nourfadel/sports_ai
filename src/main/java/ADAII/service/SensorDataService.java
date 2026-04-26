package ADAII.service;

import ADAII.dto.HardwareSensorDataRequest;
import ADAII.entity.PlayerProfile;
import ADAII.entity.SensorData;
import ADAII.entity.TrainingSession;
import ADAII.entity.enums.SessionStatus;
import ADAII.exception.InvalidSessionStateException;
import ADAII.exception.PlayerProfileNotFoundException;
import ADAII.exception.SessionNotFoundException;
import ADAII.repository.PlayerProfileRepository;
import ADAII.repository.SensorDataRepository;
import ADAII.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final SensorDataRepository sensorDataRepository;

    public void ingest(HardwareSensorDataRequest request){

        TrainingSession session = trainingSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new SessionNotFoundException("Session not found!"));

        if (session.getStatus() != SessionStatus.ACTIVE){
            throw new InvalidSessionStateException("Sensor data can only be added to an active session");
        }

        PlayerProfile profile = playerProfileRepository.findById(request.getPlayerProfileId())
                .orElseThrow(() -> new PlayerProfileNotFoundException("Player Profile not found!"));

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
