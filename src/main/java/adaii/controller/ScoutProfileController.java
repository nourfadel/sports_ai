package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.ScoutProfileRequest;
import adaii.dto.ScoutProfileResponse;
import adaii.dto.response.ScoutPlayerDetailsResponse;
import adaii.dto.response.ScoutPlayerResponse;
import adaii.service.ScoutProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scouts")
@RequiredArgsConstructor
public class ScoutProfileController {

    private final ScoutProfileService scoutProfileService;

    @PostMapping("/create-profile")
    public ResponseEntity<ApiResponse<ScoutProfileResponse>> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ScoutProfileRequest request
    ) {
        ScoutProfileResponse response =
                scoutProfileService.createScoutProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<ScoutProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Scout profile created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ScoutProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ScoutProfileResponse response =
                scoutProfileService.getMyScoutProfile(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<ScoutProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Scout profile fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ScoutProfileResponse>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ScoutProfileRequest request
    ) {
        ScoutProfileResponse response =
                scoutProfileService.updateScoutProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<ScoutProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Scout profile updated successfully")
                        .data(response)
                        .build()
        );
    }
    @GetMapping("/players")
    public ResponseEntity<ApiResponse<List<ScoutPlayerResponse>>> getAllPlayers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String teamName,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        List<ScoutPlayerResponse> response =
                scoutProfileService.getAllPlayers(
                        userDetails.getUserId(),
                        search,
                        position,
                        teamName,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(
                ApiResponse.<List<ScoutPlayerResponse>>builder()
                        .status("SUCCESS")
                        .message("Players fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/players/{playerProfileId}")
    public ResponseEntity<ApiResponse<ScoutPlayerDetailsResponse>> getPlayerDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long playerProfileId
    ) {
        ScoutPlayerDetailsResponse response =
                scoutProfileService.getPlayerDetails(userDetails.getUserId(), playerProfileId);

        return ResponseEntity.ok(
                ApiResponse.<ScoutPlayerDetailsResponse>builder()
                        .status("SUCCESS")
                        .message("Player details fetched successfully")
                        .data(response)
                        .build()
        );
    }
}