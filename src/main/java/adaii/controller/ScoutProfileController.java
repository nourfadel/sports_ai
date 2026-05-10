package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.ScoutProfileRequest;
import adaii.dto.ScoutProfileResponse;
import adaii.dto.response.ScoutDashboardResponse;
import adaii.dto.response.ScoutPlayerComparisonResponse;
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

    @PostMapping("/watchlist/{playerProfileId}")
    public ResponseEntity<ApiResponse<ScoutPlayerResponse>> addToWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long playerProfileId
    ) {
        ScoutPlayerResponse response =
                scoutProfileService.addPlayerToWatchlist(userDetails.getUserId(), playerProfileId);

        return ResponseEntity.ok(
                ApiResponse.<ScoutPlayerResponse>builder()
                        .status("SUCCESS")
                        .message("Player added to watchlist successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/watchlist")
    public ResponseEntity<ApiResponse<List<ScoutPlayerResponse>>> getWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ScoutPlayerResponse> response =
                scoutProfileService.getMyWatchlist(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<List<ScoutPlayerResponse>>builder()
                        .status("SUCCESS")
                        .message("Watchlist fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/watchlist/{playerProfileId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long playerProfileId
    ) {
        scoutProfileService.removePlayerFromWatchlist(userDetails.getUserId(), playerProfileId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("SUCCESS")
                        .message("Player removed from watchlist successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/compare")
    public ResponseEntity<ApiResponse<ScoutPlayerComparisonResponse>> comparePlayers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long player1Id,
            @RequestParam Long player2Id
    ) {
        ScoutPlayerComparisonResponse response =
                scoutProfileService.comparePlayers(
                        userDetails.getUserId(),
                        player1Id,
                        player2Id
                );

        return ResponseEntity.ok(
                ApiResponse.<ScoutPlayerComparisonResponse>builder()
                        .status("SUCCESS")
                        .message("Players compared successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<ScoutDashboardResponse>> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ScoutDashboardResponse response =
                scoutProfileService.getDashboard(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<ScoutDashboardResponse>builder()
                        .status("SUCCESS")
                        .message("Scout dashboard fetched successfully")
                        .data(response)
                        .build()
        );
    }
}