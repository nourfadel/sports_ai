package adaii.service;

import adaii.entity.AlertNotification;
import adaii.entity.PlayerProfile;
import adaii.entity.SensorData;
import adaii.entity.TrainingSession;
import adaii.repository.AlertNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertNotificationRepository alertRepository;

    public void checkAndCreateAlerts(SensorData data) {

        if (data.getHeartRate() != null && data.getHeartRate() >= 180) {
            createAlert(
                    data.getPlayerProfile(),
                    data.getSession(),
                    "HIGH_HEART_RATE",
                    "HIGH",
                    "Heart rate is too high: " + data.getHeartRate()
            );
        }

        if (data.getBodyTemperature() != null && data.getBodyTemperature() >= 39) {
            createAlert(
                    data.getPlayerProfile(),
                    data.getSession(),
                    "HIGH_BODY_TEMPERATURE",
                    "HIGH",
                    "Body temperature is too high: " + data.getBodyTemperature()
            );
        }

        if (data.getPlayerLoad() != null && data.getPlayerLoad() >= 80) {
            createAlert(
                    data.getPlayerProfile(),
                    data.getSession(),
                    "HIGH_PLAYER_LOAD",
                    "MEDIUM",
                    "Player load is high: " + data.getPlayerLoad()
            );
        }
    }

    private void createAlert(
            PlayerProfile playerProfile,
            TrainingSession session,
            String type,
            String severity,
            String message
    ) {
        AlertNotification alert = AlertNotification.builder()
                .playerProfile(playerProfile)
                .session(session)
                .type(type)
                .severity(severity)
                .message(message)
                .isRead(false)
                .build();

        alertRepository.save(alert);
    }
}