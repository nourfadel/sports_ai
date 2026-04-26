package ADAII.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ADAII.dto.CreateTrainingSessionRequest;
import ADAII.dto.TrainingSessionResponse;
import ADAII.entity.PlayerProfile;
import ADAII.entity.TrainingSession;
import ADAII.entity.enums.SessionStatus;
import ADAII.exception.InvalidSessionStateException;
import ADAII.exception.SessionNotFoundException;
import ADAII.exception.UnauthorizedSessionAccessException;
import ADAII.exception.UserNotFoundException;
import ADAII.repository.PlayerProfileRepository;
import ADAII.repository.TrainingSessionRepository;

import java.time.LocalDateTime;
import java.util.List;

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
}



















