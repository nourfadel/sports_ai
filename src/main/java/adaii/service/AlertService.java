package adaii.service;

import adaii.dto.AlertResponse;
import adaii.entity.AlertNotification;
import adaii.entity.PlayerProfile;
import adaii.entity.SensorData;
import adaii.entity.TrainingSession;
import adaii.repository.AlertNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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



    public AlertResponse markAlertAsRead(Long alertId) {
        AlertNotification alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setIsRead(true);

        AlertNotification saved = alertRepository.save(alert);

        return mapToResponse(saved);
    }

    private AlertResponse mapToResponse(AlertNotification alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .sessionId(alert.getSession().getId())
                .playerProfileId(alert.getPlayerProfile().getId())
                .type(alert.getType())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .isRead(alert.getIsRead())
                .createdAt(alert.getCreatedAt())
                .build();
    }

    public List<AlertResponse> getSessionAlerts(Long sessionId) {
        return alertRepository.findBySessionIdOrderByCreatedAtDesc(sessionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    
}