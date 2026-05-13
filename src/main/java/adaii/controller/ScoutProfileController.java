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
@RequestMapping("/api/scouts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SCOUT')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Scout APIs",
        description = "APIs for scout profile, player discovery, watchlist, comparison, and dashboard."
)
public class ScoutProfileController {

    private final ScoutProfileService scoutProfileService;

    @Operation(
            summary = "Create scout profile",
            description = "Creates a scout profile for the currently authenticated SCOUT user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scout profile created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Scout profile already exists")
    })
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

    @Operation(
            summary = "Get my scout profile",
            description = "Returns the scout profile of the currently authenticated SCOUT user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scout profile fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scout profile not found")
    })
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

    @Operation(
            summary = "Update my scout profile",
            description = "Updates the scout profile of the currently authenticated SCOUT user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scout profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scout profile not found")
    })
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

    @Operation(
            summary = "Discover players",
            description = "Returns all player profiles available for scouting with optional search, filtering, and sorting."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Players fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scout profile not found")
    })
    @GetMapping("/players")
    public ResponseEntity<ApiResponse<List<ScoutPlayerResponse>>> getAllPlayers(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Search by player name, email, team name, or position", example = "nour")
            @RequestParam(required = false) String search,

            @Parameter(description = "Filter players by position", example = "FORWARD")
            @RequestParam(required = false) String position,

            @Parameter(description = "Filter players by team name", example = "Al Ahly")
            @RequestParam(required = false) String teamName,

            @Parameter(description = "Sort field: fullName, age, position, team, overallScore, potentialScore", example = "age")
            @RequestParam(defaultValue = "fullName") String sortBy,

            @Parameter(description = "Sort direction: asc or desc", example = "desc")
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

    @Operation(
            summary = "Get player details",
            description = "Returns detailed profile information for a specific player, including latest session, live data, and AI analysis when available."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Player details fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Player profile not found")
    })
    @GetMapping("/players/{playerProfileId}")
    public ResponseEntity<ApiResponse<ScoutPlayerDetailsResponse>> getPlayerDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Player profile ID", example = "1", required = true)
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

    @Operation(
            summary = "Add player to watchlist",
            description = "Adds a specific player to the authenticated scout's watchlist."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Player added to watchlist successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Player profile or scout profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Player already exists in watchlist")
    })
    @PostMapping("/watchlist/{playerProfileId}")
    public ResponseEntity<ApiResponse<ScoutPlayerResponse>> addToWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Player profile ID", example = "1", required = true)
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

    @Operation(
            summary = "Get my watchlist",
            description = "Returns all players added to the authenticated scout's watchlist."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Watchlist fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scout profile not found")
    })
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

    @Operation(
            summary = "Remove player from watchlist",
            description = "Removes a specific player from the authenticated scout's watchlist."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Player removed from watchlist successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Player is not in watchlist")
    })
    @DeleteMapping("/watchlist/{playerProfileId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Player profile ID", example = "1", required = true)
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

    @Operation(
            summary = "Compare players",
            description = "Compares two players by returning their detailed profile, latest metrics, and analysis side by side."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Players compared successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid comparison request, such as comparing the same player"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "One or both player profiles not found")
    })
    @GetMapping("/compare")
    public ResponseEntity<ApiResponse<ScoutPlayerComparisonResponse>> comparePlayers(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "First player profile ID", example = "1", required = true)
            @RequestParam Long player1Id,

            @Parameter(description = "Second player profile ID", example = "2", required = true)
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

    @Operation(
            summary = "Get scout dashboard",
            description = "Returns dashboard summary for the authenticated scout, including total players, watchlist count, high potential count, and latest watchlist players."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Scout dashboard fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid JWT token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - user is not a SCOUT"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scout profile not found")
    })
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