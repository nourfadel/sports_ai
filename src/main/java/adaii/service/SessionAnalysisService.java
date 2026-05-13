package adaii.service;

import adaii.dto.AiAnalysisRequest;
import adaii.dto.response.AiAnalysisResponse;
import adaii.dto.SessionAnalysisResponse;
import adaii.entity.SensorData;
import adaii.entity.SessionAnalysis;
import adaii.entity.TrainingSession;
import adaii.exception.SessionNotFoundException;
import adaii.repository.SensorDataRepository;
import adaii.repository.SessionAnalysisRepository;
import adaii.repository.TrainingSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionAnalysisService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SessionAnalysisRepository sessionAnalysisRepository;
    private final AiClientService aiClientService;
    private final ObjectMapper objectMapper;

    public SessionAnalysisResponse analyzeSession(Long sessionId) {

        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        List<SensorData> sensorDataList =
                sensorDataRepository.findBySessionIdOrderByTimestampAsc(sessionId);

        if (sensorDataList.isEmpty()) {
            throw new RuntimeException("No sensor data found for this session");
        }

        AiAnalysisRequest request = buildAiRequest(session, sensorDataList);

        AiAnalysisResponse aiResponse = aiClientService.analyzeSession(request);

        SessionAnalysis analysis = sessionAnalysisRepository.findBySessionId(sessionId)
                .orElse(SessionAnalysis.builder()
                        .session(session)
                        .playerProfile(session.getPlayerProfile())
                        .build());

        analysis.setRiskLevel(aiResponse.getRiskLevel());
        analysis.setFatigueLevel(aiResponse.getFatigueLevel());
        analysis.setFinalScore(aiResponse.getFinalScore());
        analysis.setMfi(aiResponse.getMfi());
        analysis.setMlProbability(aiResponse.getMlProbability());
        analysis.setRecommendation(aiResponse.getRecommendation());
        analysis.setComponentsJson(toJson(aiResponse.getComponents()));

        SessionAnalysis saved = sessionAnalysisRepository.save(analysis);

        return mapToResponse(saved);
    }

    public SessionAnalysisResponse getSessionAnalysis(Long sessionId) {
        SessionAnalysis analysis = sessionAnalysisRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("No analysis found for this session"));

        return mapToResponse(analysis);
    }

    private AiAnalysisRequest buildAiRequest(
            TrainingSession session,
            List<SensorData> dataList
    ) {
        return AiAnalysisRequest.builder()
                .sessionId(session.getId())
                .playerProfileId(session.getPlayerProfile().getId())

                .heartRates(dataList.stream()
                        .map(SensorData::getHeartRate)
                        .filter(v -> v != null)
                        .toList())

                .hrvs(dataList.stream()
                        .map(SensorData::getHrv)
                        .filter(v -> v != null)
                        .toList())

                .speeds(dataList.stream()
                        .map(SensorData::getSpeed)
                        .filter(v -> v != null)
                        .toList())

                .distances(dataList.stream()
                        .map(SensorData::getDistance)
                        .filter(v -> v != null)
                        .toList())

                .playerLoads(dataList.stream()
                        .map(SensorData::getPlayerLoad)
                        .filter(v -> v != null)
                        .toList())

                .sprintCounts(dataList.stream()
                        .map(SensorData::getSprintCount)
                        .filter(v -> v != null)
                        .toList())

                .impactForces(dataList.stream()
                        .map(SensorData::getImpactForce)
                        .filter(v -> v != null)
                        .toList())

                .bodyTemperatures(dataList.stream()
                        .map(SensorData::getBodyTemperature)
                        .filter(v -> v != null)
                        .toList())

                .respiratoryRates(dataList.stream()
                        .map(SensorData::getRespiratoryRate)
                        .filter(v -> v != null)
                        .toList())

                .build();
    }

    private String toJson(Map<String, Double> components) {
        try {
            return objectMapper.writeValueAsString(components);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AI components to JSON");
        }
    }

    private Map<String, Double> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI components JSON");
        }
    }

    private SessionAnalysisResponse mapToResponse(SessionAnalysis analysis) {
        return SessionAnalysisResponse.builder()
                .sessionId(analysis.getSession().getId())
                .playerProfileId(analysis.getPlayerProfile().getId())
                .riskLevel(analysis.getRiskLevel())
                .fatigueLevel(analysis.getFatigueLevel())
                .finalScore(analysis.getFinalScore())
                .mfi(analysis.getMfi())
                .mlProbability(analysis.getMlProbability())
                .recommendation(analysis.getRecommendation())
                .components(fromJson(analysis.getComponentsJson()))
                .analyzedAt(analysis.getAnalyzedAt())
                .build();
    }

    public Optional<SessionAnalysisResponse> getSessionAnalysisIfExists(Long sessionId) {
        return sessionAnalysisRepository.findBySessionId(sessionId)
                .map(this::mapToResponse);
    }
}