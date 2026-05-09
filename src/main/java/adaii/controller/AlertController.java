package adaii.controller;

import adaii.dto.AlertResponse;
import adaii.dto.ApiResponse;
import adaii.entity.AlertNotification;
import adaii.repository.AlertNotificationRepository;
import adaii.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getSessionAlerts(
            @PathVariable Long sessionId
    ) {
        List<AlertResponse> response = alertService.getSessionAlerts(sessionId);

        return ResponseEntity.ok(
                ApiResponse.<List<AlertResponse>>builder()
                        .status("SUCCESS")
                        .message("Session alerts fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{alertId}/read")
    public ResponseEntity<ApiResponse<AlertResponse>> markAlertAsRead(
            @PathVariable Long alertId
    ) {
        AlertResponse response = alertService.markAlertAsRead(alertId);

        return ResponseEntity.ok(
                ApiResponse.<AlertResponse>builder()
                        .status("SUCCESS")
                        .message("Alert marked as read successfully")
                        .data(response)
                        .build()
        );
    }
}