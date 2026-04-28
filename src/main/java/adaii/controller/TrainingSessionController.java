package adaii.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.CreateTrainingSessionRequest;
import adaii.dto.TrainingSessionResponse;
import adaii.service.TrainingSessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    // create session
    @PostMapping("/create-session")
    public ResponseEntity<ApiResponse<TrainingSessionResponse>> createSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateTrainingSessionRequest request
            ){

        trainingSessionService.createSession(userDetails.getUserId(),request);

        return ResponseEntity.ok(
                ApiResponse.<TrainingSessionResponse>builder()
                        .status("Success")
                        .message("Create session successfully")
                        .data(null)
                        .build()
        );
    }

    // start session
    @PutMapping("{sessionId}/start")
    public ResponseEntity<ApiResponse<Void>> startSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long sessionId
    ){
        trainingSessionService.startSession(userDetails.getUserId(),sessionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("Success")
                        .message("Session started successfully")
                        .data(null)
                        .build()
        );
    }

    // end session
    @PutMapping("{sessionId}/end")
    public ResponseEntity<ApiResponse<Void>> endSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long sessionId
    ){
        trainingSessionService.endSession(userDetails.getUserId(),sessionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("Success")
                        .message("Session ended successfully")
                        .data(null)
                        .build()
        );
    }

    // get all session for specific player
    @GetMapping("/getAllSessions")
    public ResponseEntity<ApiResponse<List<TrainingSessionResponse>>> getAllSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        List<TrainingSessionResponse> sessions =  trainingSessionService.getMySessions(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<List<TrainingSessionResponse>>builder()
                        .status("Success")
                        .message("Session fetched successfully")
                        .data(sessions)
                        .build()
        );
    }

}
