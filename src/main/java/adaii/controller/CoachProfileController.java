package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.CoachPlayerResponse;
import adaii.dto.CoachProfileRequest;
import adaii.dto.CoachProfileResponse;
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
}