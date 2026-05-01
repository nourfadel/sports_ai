package adaii.controller;

import adaii.dto.AlertResponse;
import adaii.dto.ApiResponse;
import adaii.entity.AlertNotification;
import adaii.repository.AlertNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertNotificationRepository alertRepository;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getSessionAlerts(
            @PathVariable Long sessionId
    ) {
        List<AlertResponse> response = alertRepository.findBySessionIdOrderByCreatedAtDesc(sessionId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.<List<AlertResponse>>builder()
                        .status("SUCCESS")
                        .message("Session alerts fetched successfully")
                        .data(response)
                        .build()
        );
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
}