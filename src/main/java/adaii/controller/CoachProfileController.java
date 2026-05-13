package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.*;
import adaii.service.CoachProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COACH')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Coach APIs",
        description = "APIs for coach profile, team players, player sessions, live monitoring, alerts, AI analysis, and dashboard."
)
public class CoachProfileController {

    private final CoachProfileService coachProfileService;

    @Operation(
            summary = "Create coach profile",
            description = "Creates a coach profile for the currently authenticated COACH user and links the coach to a team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coach profile created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a COACH"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Coach profile already exists")
    })
    @PostMapping("/create-profile")
    public ResponseEntity<ApiResponse<CoachProfileResponse>> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CoachProfileRequest request
    ) {
        CoachProfileResponse response =
                coachProfileService.createCoachProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<CoachProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Coach profile created successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get my coach profile",
            description = "Returns the coach profile of the currently authenticated COACH user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coach profile fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a COACH"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coach profile not found")
    })
    @GetMapping("/getMyProfile")
    public ResponseEntity<ApiResponse<CoachProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CoachProfileResponse response =
                coachProfileService.getMyCoachProfile(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<CoachProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Coach profile fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get team players",
            description = "Returns all players that belong to the authenticated coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team players fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a COACH"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coach profile not found")
    })
    @GetMapping("/team/players")
    public ResponseEntity<ApiResponse<List<CoachPlayerResponse>>> getMyTeamPlayers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CoachPlayerResponse> response =
                coachProfileService.getMyTeamPlayers(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<List<CoachPlayerResponse>>builder()
                        .status("SUCCESS")
                        .message("Team players fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get player sessions",
            description = "Returns all training sessions for a specific player, only if the player belongs to the authenticated coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Player sessions fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - player does not belong to the coach's team"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Player profile or coach profile not found")
    })
    @GetMapping("/players/{playerProfileId}/sessions")
    public ResponseEntity<ApiResponse<List<TrainingSessionResponse>>> getPlayerSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Player profile ID", example = "1", required = true)
            @PathVariable Long playerProfileId
    ) {
        List<TrainingSessionResponse> response =
                coachProfileService.getPlayerSessions(userDetails.getUserId(), playerProfileId);

        return ResponseEntity.ok(
                ApiResponse.<List<TrainingSessionResponse>>builder()
                        .status("SUCCESS")
                        .message("Player sessions fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get session live data",
            description = "Returns the latest live sensor reading for a specific session, only if the session belongs to the coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Session live data fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - session does not belong to the coach's team"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Session or live data not found")
    })
    @GetMapping("/sessions/{sessionId}/live")
    public ResponseEntity<ApiResponse<LiveSensorDataResponse>> getLiveData(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        LiveSensorDataResponse dataResponse =
                coachProfileService.getSessionLiveData(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<LiveSensorDataResponse>builder()
                        .status("SUCCESS")
                        .message("Session live data fetched successfully")
                        .data(dataResponse)
                        .build()
        );
    }

    @Operation(
            summary = "Get session sensor history",
            description = "Returns all sensor readings for a specific session, only if the session belongs to the coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Session sensor data fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - session does not belong to the coach's team"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/sessions/{sessionId}/sensor-data")
    public ResponseEntity<ApiResponse<List<SensorDataResponse>>> getSessionHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        List<SensorDataResponse> responses =
                coachProfileService.getSessionSensorData(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<List<SensorDataResponse>>builder()
                        .status("SUCCESS")
                        .message("Session sensor data fetched successfully")
                        .data(responses)
                        .build()
        );
    }

    @Operation(
            summary = "Get session alerts",
            description = "Returns all alerts generated for a specific session, only if the session belongs to the coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Session alerts fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - session does not belong to the coach's team"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/sessions/{sessionId}/alerts")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getSessionAlerts(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        List<AlertResponse> response =
                coachProfileService.getSessionsAlerts(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<List<AlertResponse>>builder()
                        .status("SUCCESS")
                        .message("Session alerts fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get session AI analysis",
            description = "Returns the saved AI analysis result for a specific session, only if the session belongs to the coach's team."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Session analysis fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - session does not belong to the coach's team"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Session analysis not found")
    })
    @GetMapping("/sessions/{sessionId}/analysis")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> getSessionAnalysis(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        SessionAnalysisResponse response =
                coachProfileService.getSessionAnalysis(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<SessionAnalysisResponse>builder()
                        .status("SUCCESS")
                        .message("Session analysis fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get coach dashboard",
            description = "Returns dashboard summary for the authenticated coach, including team players, active sessions, unread alerts, high-risk analyses, and latest alerts."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coach dashboard fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a COACH"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coach profile not found")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<CoachDashboardResponse>> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CoachDashboardResponse response =
                coachProfileService.getDashboard(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<CoachDashboardResponse>builder()
                        .status("SUCCESS")
                        .message("Coach dashboard fetched successfully")
                        .data(response)
                        .build()
        );
    }
}