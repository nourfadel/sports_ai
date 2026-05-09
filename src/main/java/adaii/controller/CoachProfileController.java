package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.*;
import adaii.service.CoachProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor
public class CoachProfileController {

    private final CoachProfileService coachProfileService;

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

    @GetMapping("/players/{playerProfileId}/sessions")
    public ResponseEntity<ApiResponse<List<TrainingSessionResponse>>> getPlayerSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
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


    @GetMapping("/sessions/{sessionId}/live")
    public ResponseEntity<ApiResponse<LiveSensorDataResponse>> getLiveData(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long sessionId
    ){

        LiveSensorDataResponse dataResponse = coachProfileService.getSessionLiveData(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<LiveSensorDataResponse>builder()
                        .status("SUCCESS")
                        .message("Session live data fetched successfully")
                        .data(dataResponse)
                        .build()
        );

    }


    @GetMapping("/sessions/{sessionId}/sensor-data")
    public ResponseEntity<ApiResponse<List<SensorDataResponse>>> getSessionHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long sessionId){

        List<SensorDataResponse> responses = coachProfileService.getSessionSensorData(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<List<SensorDataResponse>>builder()
                        .status("SUCCESS")
                        .message("Session live data fetched successfully")
                        .data(responses)
                        .build());
    }

    @GetMapping("/sessions/{sessionId}/alerts")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getSessionAlerts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
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

    @GetMapping("/sessions/{sessionId}/analysis")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> getSessionAnalysis(
            @AuthenticationPrincipal CustomUserDetails userDetails,
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






















