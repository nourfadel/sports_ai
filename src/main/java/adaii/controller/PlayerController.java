package adaii.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import adaii.config.CustomUserDetails;
import adaii.dto.ApiResponse;
import adaii.dto.PlayerProfileRequest;
import adaii.dto.PlayerProfileResponse;
import adaii.dto.UpdatePlayerProfileRequest;
import adaii.service.PlayerProfileService;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerProfileService playerService;

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

    // method for get player profile
    @GetMapping("/getProfile")
    public ResponseEntity<ApiResponse<PlayerProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        PlayerProfileResponse profileResponse = playerService.getMyProfile(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.<PlayerProfileResponse>builder()
                        .status("SUCCESS")
                        .message("Player profile fetched successfully")
                        .data(profileResponse)
                        .build()
        );
    }

    // method for update player
    @PostMapping("/updateProfile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UpdatePlayerProfileRequest request
    ){
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
