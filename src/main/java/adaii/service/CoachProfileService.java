package adaii.service;

import adaii.dto.*;
import adaii.entity.*;
import adaii.entity.enums.SessionStatus;
import adaii.exception.SessionNotFoundException;
import adaii.exception.UnauthorizedSessionAccessException;
import adaii.exception.UserAlreadyExistsException;
import adaii.exception.UserNotFoundException;
import adaii.repository.*;
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
    private final TrainingSessionRepository trainingSessionRepository;
    private final SensorDataService sensorDataService;
    private final AlertNotificationRepository alertNotificationRepository;
    private final SessionAnalysisService sessionAnalysisService;
    private final SessionAnalysisRepository sessionAnalysisRepository;


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

    public List<TrainingSessionResponse> getPlayerSessions(Long coachId, Long playerId){

        CoachProfile coachProfile = coachProfileRepository.findByUserId(coachId)
                .orElseThrow(() -> new UserNotFoundException("Coach profile not found"));

        PlayerProfile playerProfile = playerProfileRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        if (playerProfile.getTeam() == null || !playerProfile.getTeam().getId().equals(coachProfile.getTeam().getId())){
            throw new UnauthorizedSessionAccessException("This player does not belong to your team");
        }

        return trainingSessionRepository.findByPlayerProfileIdOrderByCreatedAtDesc(playerId)
                .stream()
                .map(this::mapSessionToResponse)
                .toList();
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


    public LiveSensorDataResponse getSessionLiveData(Long coachId,Long sessionId){
        getSessionIfBelongToCoachTeam(coachId,sessionId);

        return sensorDataService.getLiveData(sessionId);
    }

    private TrainingSession getSessionIfBelongToCoachTeam(Long coachId,Long sessionId){

        CoachProfile coachProfile = coachProfileRepository.findByUserId(coachId)
                .orElseThrow(() -> new UserNotFoundException("Coach profile not found!"));

        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found!"));

//        if (session.getPlayerProfile().getTeam() == null ||
//                !session.getPlayerProfile().getTeam().getId().equals(coachProfile.getTeam().getId())){
//            throw new UnauthorizedSessionAccessException("This player does not belong to your team");
//        }
//
        Long coachTeamId = coachProfile.getTeam().getId();
        Long playerTeamId = session.getPlayerProfile().getTeam().getId();

        if (!coachTeamId.equals(playerTeamId)) {
            throw new UnauthorizedSessionAccessException("This session does not belong to your team");
        }


        return session;
    }

    public List<SensorDataResponse> getSessionSensorData(Long coachId,Long sessionId){
        getSessionIfBelongToCoachTeam(coachId,sessionId);

        List<SensorDataResponse> responses = sensorDataService.getSessionData(sessionId);

        return responses;
    }


    public List<AlertResponse> getSessionsAlerts(Long coachId,Long sessionId){
        getSessionIfBelongToCoachTeam(coachId,sessionId);

        return alertNotificationRepository.findBySessionIdOrderByCreatedAtDesc(sessionId)
                .stream()
                .map(this::mapAlertToResponse)
                .toList();
    }

    private AlertResponse mapAlertToResponse(AlertNotification alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .sessionId(alert.getSession().getId())
                .playerProfileId(alert.getPlayerProfile().getId())
                .type(alert.getType())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .isRead(alert.getIsRead())
                .createdAt(alert.getCreatedAt())
                .build();
    }

    public SessionAnalysisResponse getSessionAnalysis(Long coachId,Long sessionId){
        getSessionIfBelongToCoachTeam(coachId,sessionId);

        return sessionAnalysisService.getSessionAnalysis(sessionId);
    }

    public CoachDashboardResponse getDashboard(Long coachUserId) {

        CoachProfile coachProfile = coachProfileRepository.findByUserId(coachUserId)
                .orElseThrow(() -> new UserNotFoundException("Coach profile not found"));

        Long teamId = coachProfile.getTeam().getId();

        List<CoachPlayerResponse> players = playerProfileRepository.findByTeamId(teamId)
                .stream()
                .map(this::mapPlayerToCoachResponse)
                .toList();

        Integer activeSessionsCount =
                trainingSessionRepository.findByTeamIdAndStatus(teamId, SessionStatus.ACTIVE).size();

        Integer unreadAlertsCount =
                alertNotificationRepository.countBySessionTeamIdAndIsReadFalse(teamId);

        Integer highRiskAnalysesCount =
                sessionAnalysisRepository.countBySessionTeamIdAndRiskLevel(teamId, "HIGH_RISK");

        List<AlertResponse> latestAlerts =
                alertNotificationRepository.findTop5BySessionTeamIdOrderByCreatedAtDesc(teamId)
                        .stream()
                        .map(this::mapAlertToResponse)
                        .toList();

        return CoachDashboardResponse.builder()
                .teamName(coachProfile.getTeam().getName())
                .playersCount(players.size())
                .activeSessionsCount(activeSessionsCount)
                .unreadAlertsCount(unreadAlertsCount)
                .highRiskAnalysesCount(highRiskAnalysesCount)
                .teamPlayers(players)
                .latestAlerts(latestAlerts)
                .build();
    }
}



























