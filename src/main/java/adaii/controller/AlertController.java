package adaii.controller;

import adaii.dto.AlertResponse;
import adaii.dto.ApiResponse;
import adaii.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PLAYER', 'COACH')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Alert APIs",
        description = "APIs for retrieving session alerts and marking alerts as read."
)
public class AlertController {

    private final AlertService alertService;

    @Operation(
            summary = "Get session alerts",
            description = "Returns all alerts generated for a specific training session. Accessible by PLAYER and COACH roles."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session alerts fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not allowed to access alerts"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Session not found"
            )
    })
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getSessionAlerts(
            @Parameter(
                    description = "Training session ID",
                    example = "2",
                    required = true
            )
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

    @Operation(
            summary = "Mark alert as read",
            description = "Marks a specific alert notification as read. Accessible by PLAYER and COACH roles."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Alert marked as read successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not allowed to update this alert"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Alert not found"
            )
    })
    @PutMapping("/{alertId}/read")
    public ResponseEntity<ApiResponse<AlertResponse>> markAlertAsRead(
            @Parameter(
                    description = "Alert notification ID",
                    example = "6",
                    required = true
            )
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