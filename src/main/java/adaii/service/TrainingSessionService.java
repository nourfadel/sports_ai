package adaii.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import adaii.dto.CreateTrainingSessionRequest;
import adaii.dto.TrainingSessionResponse;
import adaii.entity.PlayerProfile;
import adaii.entity.TrainingSession;
import adaii.entity.enums.SessionStatus;
import adaii.exception.InvalidSessionStateException;
import adaii.exception.SessionNotFoundException;
import adaii.exception.UnauthorizedSessionAccessException;
import adaii.exception.UserNotFoundException;
import adaii.repository.PlayerProfileRepository;
import adaii.repository.TrainingSessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {

    private final PlayerProfileRepository playerProfileRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    // create session method
    public void createSession(Long userId, CreateTrainingSessionRequest request){
        PlayerProfile playerProfile = playerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        TrainingSession session = TrainingSession.builder()
                .playerProfile(playerProfile)
                .team(playerProfile.getTeam())
                .sessionType(request.getSessionType())
                .status(SessionStatus.SCHEDULED)
                .scheduledAt(request.getScheduledAt())
                .notes(request.getNotes())
                .build();

        trainingSessionRepository.save(session);
    }

    // start session method
    public void startSession(Long userId,Long sessionId){
        TrainingSession session = getOwnedSession(userId,sessionId);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new InvalidSessionStateException("Only scheduled sessions can be started");
        }


        session.setStatus(SessionStatus.ACTIVE);
        session.setStartedAt(LocalDateTime.now());
        trainingSessionRepository.save(session);
    }

    // end session method
    public void endSession(Long userId,Long sessionId){
        TrainingSession session = getOwnedSession(userId,sessionId);

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new InvalidSessionStateException("Only active sessions can be ended");
        }

        session.setStatus(SessionStatus.FINISHED);
        session.setEndedAt(LocalDateTime.now());
        trainingSessionRepository.save(session);
    }

    // get all sessions
    public List<TrainingSessionResponse> getMySessions(Long userId){
        PlayerProfile playerProfile = playerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        return trainingSessionRepository.findByPlayerProfileIdOrderByCreatedAtDesc(playerProfile.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }



    // get owned session
    private TrainingSession getOwnedSession(Long userId, Long sessionId) {
        PlayerProfile playerProfile = playerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Player profile not found"));

        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if (!session.getPlayerProfile().getId().equals(playerProfile.getId())){
            throw new UnauthorizedSessionAccessException("You are not allowed to access this session!");
        }

        return session;
    }

    // map function
    private TrainingSessionResponse mapToResponse(TrainingSession session) {
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

    public Optional<Long> getLatestSessionIdForPlayer(Long playerProfileId) {
        return trainingSessionRepository
                .findTopByPlayerProfileIdOrderByCreatedAtDesc(playerProfileId)
                .map(TrainingSession::getId);
    }
}



















