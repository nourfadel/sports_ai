package adaii.service;

import adaii.dto.*;
import adaii.dto.response.*;
import adaii.entity.*;
import adaii.exception.UserAlreadyExistsException;
import adaii.exception.UserNotFoundException;
import adaii.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoutProfileService {

    private final ScoutProfileRepository scoutProfileRepository;
    private final UserRepository userRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final SensorDataService sensorDataService;
    private final SessionAnalysisService sessionAnalysisService;
    private final ScoutWatchlistRepository scoutWatchlistRepository;
    private final PlayerScoringService playerScoringService;

    public ScoutProfileResponse createScoutProfile(Long userId, ScoutProfileRequest request) {

        if (scoutProfileRepository.existsByUserId(userId)) {
            throw new UserAlreadyExistsException("Scout profile already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ScoutProfile scoutProfile = ScoutProfile.builder()
                .user(user)
                .age(request.getAge())
                .organizationName(request.getOrganizationName())
                .region(request.getRegion())
                .build();

        user.setProfileCompleted(true);

        ScoutProfile saved = scoutProfileRepository.save(scoutProfile);
        userRepository.save(user);

        return mapToResponse(saved);
    }

    public ScoutProfileResponse getMyScoutProfile(Long userId) {
        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        return mapToResponse(scoutProfile);
    }

    public ScoutProfileResponse updateScoutProfile(Long userId, ScoutProfileRequest request) {
        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        scoutProfile.setAge(request.getAge());
        scoutProfile.setOrganizationName(request.getOrganizationName());
        scoutProfile.setRegion(request.getRegion());

        ScoutProfile saved = scoutProfileRepository.save(scoutProfile);

        return mapToResponse(saved);
    }

    private ScoutProfileResponse mapToResponse(ScoutProfile scoutProfile) {
        User user = scoutProfile.getUser();

        return ScoutProfileResponse.builder()
                .id(scoutProfile.getId())
                .userId(user.getId())
                .fullName(buildFullName(user))
                .email(user.getEmail())
                .age(scoutProfile.getAge())
                .organizationName(scoutProfile.getOrganizationName())
                .region(scoutProfile.getRegion())
                .build();
    }

    private String buildFullName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";

        String fullName = (firstName + " " + lastName).trim();

        return fullName.isBlank() ? user.getEmail() : fullName;
    }


    public List<ScoutPlayerResponse> getAllPlayers(
            Long scoutUserId,
            String search,
            String position,
            String teamName,
            String sortBy,
            String direction
    ) {
        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        List<ScoutPlayerResponse> players = playerProfileRepository.findAll()
                .stream()
                .map(player -> {
                    boolean watchlisted =
                            scoutWatchlistRepository.existsByScoutProfileIdAndPlayerProfileId(
                                    scoutProfile.getId(),
                                    player.getId()
                            );

                    return mapPlayerToScoutResponse(player, watchlisted);
                })
                .filter(player -> matchesSearch(player, search))
                .filter(player -> matchesPosition(player, position))
                .filter(player -> matchesTeam(player, teamName))
                .sorted(getScoutPlayerComparator(sortBy, direction))
                .toList();

        return players;
    }

    private boolean matchesSearch(ScoutPlayerResponse player, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }

        String keyword = search.toLowerCase();

        return player.getFullName().toLowerCase().contains(keyword)
                || player.getEmail().toLowerCase().contains(keyword)
                || (player.getTeamName() != null && player.getTeamName().toLowerCase().contains(keyword))
                || (player.getPosition() != null && player.getPosition().toLowerCase().contains(keyword));
    }

    private boolean matchesPosition(ScoutPlayerResponse player, String position) {
        if (position == null || position.isBlank()) {
            return true;
        }

        return player.getPosition() != null
                && player.getPosition().equalsIgnoreCase(position);
    }

    private boolean matchesTeam(ScoutPlayerResponse player, String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return true;
        }

        return player.getTeamName() != null
                && player.getTeamName().equalsIgnoreCase(teamName);
    }

    private Comparator<ScoutPlayerResponse> getScoutPlayerComparator(
            String sortBy,
            String direction
    ) {
        Comparator<ScoutPlayerResponse> comparator;

        switch (sortBy.toLowerCase()) {
            case "age" -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getAge,
                    Comparator.nullsLast(Integer::compareTo)
            );

            case "position" -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getPosition,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );

            case "team" -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getTeamName,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );

            case "overallscore" -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getOverallScore,
                    Comparator.nullsLast(Double::compareTo)
            );

            case "potentialscore" -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getPotentialScore,
                    Comparator.nullsLast(Double::compareTo)
            );

            default -> comparator = Comparator.comparing(
                    ScoutPlayerResponse::getFullName,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private ScoutPlayerResponse mapPlayerToScoutResponse(PlayerProfile playerProfile,Boolean watchList) {

        User user = playerProfile.getUser();

        PlayerScoreResponse score = playerScoringService.calculateScores(playerProfile);

        return ScoutPlayerResponse.builder()
                .playerProfileId(playerProfile.getId())
                .userId(user.getId())
                .fullName(buildFullName(user))
                .email(user.getEmail())
                .age(playerProfile.getAge())
                .position(playerProfile.getPlayerPosition().name())
                .teamName(playerProfile.getTeam() != null ? playerProfile.getTeam().getName() : null)
                .playerImageUrl(playerProfile.getPlayerImageUrl())
                .heightCm(playerProfile.getHeightCm())
                .weightKg(playerProfile.getWeightKg())
                .overallScore(score.getOverallScore())
                .potentialScore(score.getPotentialScore())
                .watchlisted(watchList)
                .build();
    }


    public ScoutPlayerDetailsResponse getPlayerDetails(Long scoutUserId, Long playerProfileId) {

        scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        PlayerProfile playerProfile = playerProfileRepository.findById(playerProfileId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        TrainingSession latestSession = trainingSessionRepository
                .findTopByPlayerProfileIdOrderByCreatedAtDesc(playerProfileId)
                .orElse(null);

        TrainingSessionResponse latestSessionResponse = null;
        LiveSensorDataResponse latestLiveData = null;
        SessionAnalysisResponse latestAnalysis = null;

        if (latestSession != null) {
            latestSessionResponse = mapSessionToResponse(latestSession);

            latestLiveData = sensorDataService
                    .getLatestLiveDataIfExists(latestSession.getId())
                    .orElse(null);

            latestAnalysis = sessionAnalysisService
                    .getSessionAnalysisIfExists(latestSession.getId())
                    .orElse(null);
        }

        return ScoutPlayerDetailsResponse.builder()
                .playerProfileId(playerProfile.getId())
                .userId(playerProfile.getUser().getId())
                .fullName(buildFullName(playerProfile.getUser()))
                .email(playerProfile.getUser().getEmail())
                .age(playerProfile.getAge())
                .position(playerProfile.getPlayerPosition().name())
                .teamName(playerProfile.getTeam() != null ? playerProfile.getTeam().getName() : null)
                .playerImageUrl(playerProfile.getPlayerImageUrl())
                .heightCm(playerProfile.getHeightCm())
                .weightKg(playerProfile.getWeightKg())
                .overallScore(0.0)
                .potentialScore(0.0)
                .watchlisted(false)
                .latestSession(latestSessionResponse)
                .latestLiveData(latestLiveData)
                .latestAnalysis(latestAnalysis)
                .build();
    }

    private TrainingSessionResponse mapSessionToResponse(TrainingSession session) {
        return TrainingSessionResponse.builder()
                .id(session.getId())
                .sessionType(session.getSessionType())
                .status(session.getStatus())
                .scheduledAt(session.getScheduledAt())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .notes(session.getNotes())
                .teamName(session.getTeam() != null ? session.getTeam().getName() : null)
                .build();
    }

    public ScoutPlayerResponse addPlayerToWatchlist(Long scoutUserId, Long playerProfileId) {

        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        PlayerProfile playerProfile = playerProfileRepository.findById(playerProfileId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        if (scoutWatchlistRepository.existsByScoutProfileIdAndPlayerProfileId(
                scoutProfile.getId(),
                playerProfile.getId()
        )) {
            throw new UserAlreadyExistsException("Player already exists in your watchlist");
        }

        ScoutWatchlist watchlist = ScoutWatchlist.builder()
                .scoutProfile(scoutProfile)
                .playerProfile(playerProfile)
                .build();

        scoutWatchlistRepository.save(watchlist);

        return mapPlayerToScoutResponse(playerProfile, true);
    }

    public List<ScoutPlayerResponse> getMyWatchlist(Long scoutUserId) {

        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        return scoutWatchlistRepository.findByScoutProfileIdOrderByCreatedAtDesc(scoutProfile.getId())
                .stream()
                .map(item -> mapPlayerToScoutResponse(item.getPlayerProfile(), true))
                .toList();
    }

    public void removePlayerFromWatchlist(Long scoutUserId, Long playerProfileId) {

        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        ScoutWatchlist watchlist = scoutWatchlistRepository
                .findByScoutProfileIdAndPlayerProfileId(scoutProfile.getId(), playerProfileId)
                .orElseThrow(() -> new RuntimeException("Player is not in your watchlist"));

        scoutWatchlistRepository.delete(watchlist);
    }

    public ScoutPlayerComparisonResponse comparePlayers(
            Long scoutUserId,
            Long player1Id,
            Long player2Id
    ) {
        if (player1Id.equals(player2Id)) {
            throw new IllegalArgumentException("You cannot compare the same player");
        }

        ScoutPlayerDetailsResponse player1 =
                getPlayerDetails(scoutUserId, player1Id);

        ScoutPlayerDetailsResponse player2 =
                getPlayerDetails(scoutUserId, player2Id);

        return ScoutPlayerComparisonResponse.builder()
                .player1(player1)
                .player2(player2)
                .build();
    }

    public ScoutDashboardResponse getDashboard(Long scoutUserId) {

        ScoutProfile scoutProfile = scoutProfileRepository.findByUserId(scoutUserId)
                .orElseThrow(() -> new UserNotFoundException("Scout profile not found"));

        List<PlayerProfile> allPlayers = playerProfileRepository.findAll();

        List<ScoutWatchlist> watchlistItems =
                scoutWatchlistRepository.findByScoutProfileIdOrderByCreatedAtDesc(scoutProfile.getId());

        List<ScoutPlayerResponse> latestWatchlistPlayers = watchlistItems.stream()
                .limit(5)
                .map(item -> mapPlayerToScoutResponse(item.getPlayerProfile(), true))
                .toList();

        int highPotentialCount = allPlayers.stream()
                .map(player -> mapPlayerToScoutResponse(player, false))
                .filter(player -> player.getPotentialScore() != null && player.getPotentialScore() >= 80)
                .toList()
                .size();

        return ScoutDashboardResponse.builder()
                .playersCount(allPlayers.size())
                .watchlistCount(watchlistItems.size())
                .highPotentialCount(highPotentialCount)
                .latestWatchlistPlayers(latestWatchlistPlayers)
                .build();
    }


}