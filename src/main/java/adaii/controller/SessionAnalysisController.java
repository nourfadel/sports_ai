package adaii.controller;

import adaii.dto.ApiResponse;
import adaii.dto.SessionAnalysisResponse;
import adaii.service.SessionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "AI Analysis APIs",
        description = "APIs for analyzing training sessions using the FastAPI AI service and retrieving saved AI analysis results."
)
public class SessionAnalysisController {

    private final SessionAnalysisService sessionAnalysisService;

    @Operation(
            summary = "Analyze session",
            description = "Sends session sensor data to the FastAPI AI service, receives risk/fatigue analysis, saves the result, and returns it."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session analyzed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or session has no sensor data"
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
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "AI service is unavailable"
            )
    })
    @PostMapping("/{sessionId}/analyze")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> analyzeSession(
            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        SessionAnalysisResponse response =
                sessionAnalysisService.analyzeSession(sessionId);

        return ResponseEntity.ok(
                ApiResponse.<SessionAnalysisResponse>builder()
                        .status("SUCCESS")
                        .message("Session analyzed successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Get session analysis",
            description = "Returns the saved AI analysis result for a specific training session."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Session analysis fetched successfully"
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
                    description = "Session analysis not found"
            )
    })
    @GetMapping("/{sessionId}/analysis")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> getSessionAnalysis(
            @Parameter(description = "Training session ID", example = "2", required = true)
            @PathVariable Long sessionId
    ) {
        SessionAnalysisResponse response =
                sessionAnalysisService.getSessionAnalysis(sessionId);

        return ResponseEntity.ok(
                ApiResponse.<SessionAnalysisResponse>builder()
                        .status("SUCCESS")
                        .message("Session analysis fetched successfully")
                        .data(response)
                        .build()
        );
    }
}