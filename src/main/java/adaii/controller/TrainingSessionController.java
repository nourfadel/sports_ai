package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.CreateTrainingSessionRequest;
import adaii.dto.TrainingSessionResponse;
import adaii.service.TrainingSessionService;
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
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Training Session APIs",
        description = "APIs for creating, starting, ending, and retrieving player training sessions."
)
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    @Operation(
            summary = "Create training session",
            description = "Creates a new scheduled training session for the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Training session created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Player profile not found"
            )
    })
    @PostMapping("/create-session")
    public ResponseEntity<ApiResponse<TrainingSessionResponse>> createSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateTrainingSessionRequest request
    ) {
        trainingSessionService.createSession(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<TrainingSessionResponse>builder()
                        .status("SUCCESS")
                        .message("Session created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "Start training session",
            description = "Starts a scheduled training session owned by the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session started successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Only scheduled sessions can be started"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER or does not own this session"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Session or player profile not found"
            )
    })
    @PutMapping("/{sessionId}/start")
    public ResponseEntity<ApiResponse<Void>> startSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        trainingSessionService.startSession(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("SUCCESS")
                        .message("Session started successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "End training session",
            description = "Ends an active training session owned by the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session ended successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Only active sessions can be ended"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER or does not own this session"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Session or player profile not found"
            )
    })
    @PutMapping("/{sessionId}/end")
    public ResponseEntity<ApiResponse<Void>> endSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        trainingSessionService.endSession(userDetails.getUserId(), sessionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("SUCCESS")
                        .message("Session ended successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "Get my training sessions",
            description = "Returns all training sessions created by the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Sessions fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - user is not a PLAYER"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Player profile not found"
            )
    })
    @GetMapping("/getAllSessions")
    public ResponseEntity<ApiResponse<List<TrainingSessionResponse>>> getAllSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<TrainingSessionResponse> sessions =
                trainingSessionService.getMySessions(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<List<TrainingSessionResponse>>builder()
                        .status("SUCCESS")
                        .message("Sessions fetched successfully")
                        .data(sessions)
                        .build()
        );
    }
}