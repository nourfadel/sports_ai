package sports_ai_system.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sports_ai_system.dto.PlayerProfileRequest;
import sports_ai_system.dto.PlayerProfileResponse;
import sports_ai_system.dto.UpdatePlayerProfileRequest;
import sports_ai_system.entity.PlayerProfile;
import sports_ai_system.entity.Team;
import sports_ai_system.entity.User;
import sports_ai_system.exception.UserNotFoundException;
import sports_ai_system.repository.PlayerProfileRepository;
import sports_ai_system.repository.TeamRepository;
import sports_ai_system.repository.UserRepository;

@Service
@AllArgsConstructor
public class PlayerProfileService {

    private final UserRepository userRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final TeamRepository teamRepository;

    // create a profile
    public void createPlayerProfile(Long userId,PlayerProfileRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found!"));

        Team team = teamRepository.findByName(request.getTeamName())
                .orElseGet(() -> {
                    Team newTeam = Team.builder()
                            .name(request.getTeamName())
                            .build();
                    return teamRepository.save(newTeam);
                });

        PlayerProfile playerProfile = PlayerProfile.builder()
                .user(user)
                .age(request.getAge())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .playerPosition(request.getPrimaryPosition())
                .gender(request.getGender())
                .team(team)
                .build();

        playerProfileRepository.save(playerProfile);
        user.markProfileCompleted();
        userRepository.save(user);

    }

    public PlayerProfileResponse getMyProfile(Long id){
        PlayerProfile profile = playerProfileRepository.findByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found!"));

        return mapToResponse(profile);
    }

    public void updateMyProfile(Long userId,UpdatePlayerProfileRequest request){
        PlayerProfile profile = playerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        profile.setAge(request.getAge());
        profile.setHeightCm(request.getHeightCm());
        profile.setWeightKg(request.getWeightKg());
        profile.setGender(request.getGender());
        profile.setPlayerPosition(request.getPrimaryPosition());
        profile.setPlayerImageUrl(request.getProfileImageUrl());

        playerProfileRepository.save(profile);
    }

    private PlayerProfileResponse mapToResponse(PlayerProfile profile) {
        return PlayerProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getUser().getFirstName() + " " + profile.getUser().getLastName())
                .email(profile.getUser().getEmail())
                .age(profile.getAge())
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .gender(profile.getGender())
                .primaryPosition(profile.getPlayerPosition())
                .teamName(profile.getTeam() != null ? profile.getTeam().getName() : null)
                .profileImageUrl(profile.getPlayerImageUrl())
                .build();
    }

}
