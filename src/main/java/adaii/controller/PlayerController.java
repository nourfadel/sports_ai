package adaii.controller;

import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.PlayerProfileRequest;
import adaii.dto.PlayerProfileResponse;
import adaii.dto.UpdatePlayerProfileRequest;
import adaii.service.PlayerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('PLAYER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Player APIs",
        description = "APIs for creating, retrieving, and updating player profiles."
)
@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerProfileService playerService;

    @Operation(
            summary = "Create player profile",
            description = "Creates a player profile for the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Player profile created successfully"
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
                    responseCode = "409",
                    description = "Player profile already exists"
            )
    })
    @PostMapping("/create-profile")
    public ResponseEntity<ApiResponse<Void>> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PlayerProfileRequest request
    ) {
        playerService.createPlayerProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("SUCCESS")
                        .message("Player profile created")
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "Get my player profile",
            description = "Returns the player profile for the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Player profile fetched successfully"
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
    @GetMapping("/getProfile")
    public ResponseEntity<ApiResponse<PlayerProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PlayerProfileResponse profileResponse =
                playerService.getMyProfile(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<PlayerProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Player profile fetched successfully")
                        .data(profileResponse)
                        .build()
        );
    }

    @Operation(
            summary = "Update my player profile",
            description = "Updates the player profile for the currently authenticated PLAYER user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Player profile updated successfully"
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
    @PostMapping("/updateProfile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdatePlayerProfileRequest request
    ) {
        playerService.updateMyProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("SUCCESS")
                        .message("Player profile updated successfully")
                        .data(null)
                        .build()
        );
    }
}