package adaii.service;

import adaii.dto.CoachPlayerResponse;
import adaii.dto.CoachProfileRequest;
import adaii.dto.CoachProfileResponse;
import adaii.entity.CoachProfile;
import adaii.entity.PlayerProfile;
import adaii.entity.Team;
import adaii.entity.User;
import adaii.exception.UserAlreadyExistsException;
import adaii.exception.UserNotFoundException;
import adaii.repository.CoachProfileRepository;
import adaii.repository.PlayerProfileRepository;
import adaii.repository.TeamRepository;
import adaii.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachProfileService {

    private final CoachProfileRepository coachProfileRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerProfileRepository playerProfileRepository;

    public CoachProfileResponse createCoachProfile(Long userId, CoachProfileRequest request) {

        if (coachProfileRepository.existsByUserId(userId)) {
            throw new UserAlreadyExistsException("Coach profile already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Team team = teamRepository.findByName(request.getTeamName())
                .orElseGet(() -> teamRepository.save(
                        Team.builder()
                                .name(request.getTeamName())
                                .build()
                ));

        CoachProfile coachProfile = CoachProfile.builder()
                .user(user)
                .team(team)
                .specialization(request.getSpecialization())
                .yearsOfExperience(request.getYearsOfExperience())
                .licenseNumber(request.getLicenseNumber())
                .build();

        user.setProfileCompleted(true);

        CoachProfile saved = coachProfileRepository.save(coachProfile);
        userRepository.save(user);

        return mapToResponse(saved);
    }

    public CoachProfileResponse getMyCoachProfile(Long userId) {
        CoachProfile coachProfile = coachProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Coach profile not found"));

        return mapToResponse(coachProfile);
    }

    private CoachProfileResponse mapToResponse(CoachProfile coachProfile) {
        User user = coachProfile.getUser();

        return CoachProfileResponse.builder()
                .id(coachProfile.getId())
                .userId(user.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .teamName(coachProfile.getTeam().getName())
                .specialization(coachProfile.getSpecialization())
                .yearsOfExperience(coachProfile.getYearsOfExperience())
                .licenseNumber(coachProfile.getLicenseNumber())
                .build();
    }

    public List<CoachPlayerResponse> getMyTeamPlayers(Long userId){

        CoachProfile coachProfile = coachProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Coach not found!"));

        Long teamId = coachProfile.getTeam().getId();

        return playerProfileRepository.findByTeamId(teamId)
                .stream()
                .map(this::mapPlayerToCoachResponse)
                .toList();
    }

    private CoachPlayerResponse mapPlayerToCoachResponse(PlayerProfile playerProfile) {

        User user = playerProfile.getUser();

        return CoachPlayerResponse.builder()
                .playerProfileId(playerProfile.getId())
                .userId(user.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .position(playerProfile.getPlayerPosition().name())
                .age(playerProfile.getAge())
                .heightCm(playerProfile.getHeightCm())
                .weightKg(playerProfile.getWeightKg())
                .teamName(playerProfile.getTeam() != null ? playerProfile.getTeam().getName() : null)
                .playerImageUrl(playerProfile.getPlayerImageUrl())
                .build();
    }

}