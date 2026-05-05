package adaii.controller;

import adaii.dto.ApiResponse;
import adaii.dto.SessionAnalysisResponse;
import adaii.service.SessionAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionAnalysisController {

    private final SessionAnalysisService sessionAnalysisService;

    @PostMapping("/{sessionId}/analyze")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> analyzeSession(
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

    @GetMapping("/{sessionId}/analysis")
    public ResponseEntity<ApiResponse<SessionAnalysisResponse>> getSessionAnalysis(
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