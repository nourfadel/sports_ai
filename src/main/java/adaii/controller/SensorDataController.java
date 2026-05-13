package adaii.controller;

import adaii.dto.ApiResponse;
import adaii.dto.LiveSensorDataResponse;
import adaii.dto.SensorDataResponse;
import adaii.dto.SessionSummaryResponse;
import adaii.service.SensorDataService;
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
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Sensor Data APIs",
        description = "APIs for retrieving live sensor data, session sensor history, and session summary."
)
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Operation(
            summary = "Get live sensor data",
            description = "Returns the latest sensor reading for a specific training session."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Live sensor data fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER or cannot access this session"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No live data found for this session"
            )
    })
    @GetMapping("/{sessionId}/live")
    public ResponseEntity<ApiResponse<LiveSensorDataResponse>> getLiveData(
            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        LiveSensorDataResponse response = sensorDataService.getLiveData(sessionId);

        return ResponseEntity.ok(
                ApiResponse.<LiveSensorDataResponse>builder()
                        .status("SUCCESS")
                        .message("Live sensor data fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get session sensor history",
            description = "Returns all sensor readings recorded for a specific training session, ordered by timestamp."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session sensor data fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER or cannot access this session"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Session not found"
            )
    })
    @GetMapping("/{sessionId}/sensor-data")
    public ResponseEntity<ApiResponse<List<SensorDataResponse>>> getSessionData(
            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<SensorDataResponse>>builder()
                        .status("SUCCESS")
                        .message("Session sensor data fetched successfully")
                        .data(sensorDataService.getSessionData(sessionId))
                        .build()
        );
    }

    @Operation(
            summary = "Get session summary",
            description = "Returns aggregated performance metrics for a specific training session such as average heart rate, max speed, total distance, and sprint count."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session summary fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER or cannot access this session"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No sensor data found for this session"
            )
    })
    @GetMapping("/{sessionId}/summary")
    public ResponseEntity<ApiResponse<SessionSummaryResponse>> getSessionSummary(
            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<SessionSummaryResponse>builder()
                        .status("SUCCESS")
                        .message("Session summary fetched successfully")
                        .data(sensorDataService.getSessionSummary(sessionId))
                        .build()
        );
    }
}