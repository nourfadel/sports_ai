package adaii.service;

import adaii.dto.response.PlayerScoreResponse;
import adaii.dto.SessionAnalysisResponse;
import adaii.dto.SessionSummaryResponse;
import adaii.entity.PlayerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerScoringService {

    private final SensorDataService sensorDataService;
    private final SessionAnalysisService sessionAnalysisService;
    private final TrainingSessionService trainingSessionService;

    public PlayerScoreResponse calculateScores(PlayerProfile playerProfile) {

        Long latestSessionId = trainingSessionService
                .getLatestSessionIdForPlayer(playerProfile.getId())
                .orElse(null);

        if (latestSessionId == null) {
            return PlayerScoreResponse.builder()
                    .overallScore(0.0)
                    .potentialScore(calculateBasePotentialByAge(playerProfile.getAge()))
                    .build();
        }

        SessionSummaryResponse summary = null;
        SessionAnalysisResponse analysis = null;

        try {
            summary = sensorDataService.getSessionSummary(latestSessionId);
        } catch (Exception ignored) {
            // no sensor data yet
        }

        analysis = sessionAnalysisService
                .getSessionAnalysisIfExists(latestSessionId)
                .orElse(null);

        double overall = calculateOverallScore(summary, analysis);
        double potential = calculatePotentialScore(playerProfile, overall, analysis);

        return PlayerScoreResponse.builder()
                .overallScore(round(overall))
                .potentialScore(round(potential))
                .build();
    }

    private double calculateOverallScore(SessionSummaryResponse summary, SessionAnalysisResponse analysis) {
        if (summary == null) {
            return 0.0;
        }

        double speedScore = normalize(summary.getMaxSpeed(), 0, 10) * 25;
        double distanceScore = normalize(summary.getTotalDistance(), 0, 5000) * 20;
        double sprintScore = normalize(summary.getTotalSprints(), 0, 30) * 15;
        double loadScore = normalize(summary.getAvgPlayerLoad(), 0, 100) * 15;
        double heartScore = calculateHeartRateScore(summary.getAvgHeartRate()) * 15;
        double analysisBonus = calculateAnalysisBonus(analysis) * 10;

        return clamp(speedScore + distanceScore + sprintScore + loadScore + heartScore + analysisBonus, 0, 100);
    }

    private double calculatePotentialScore(PlayerProfile playerProfile, double overallScore, SessionAnalysisResponse analysis) {
        double ageScore = calculateBasePotentialByAge(playerProfile.getAge());

        double riskModifier = 0;

        if (analysis != null && analysis.getRiskLevel() != null) {
            switch (analysis.getRiskLevel()) {
                case "LOW_RISK" -> riskModifier = 8;
                case "MODERATE_RISK" -> riskModifier = 0;
                case "HIGH_RISK" -> riskModifier = -12;
                default -> riskModifier = 0;
            }
        }

        double potential = (overallScore * 0.6) + (ageScore * 0.4) + riskModifier;

        return clamp(potential, 0, 100);
    }

    private double calculateBasePotentialByAge(Integer age) {
        if (age == null) return 50;

        if (age <= 18) return 95;
        if (age <= 21) return 88;
        if (age <= 24) return 78;
        if (age <= 28) return 65;
        if (age <= 32) return 50;

        return 35;
    }

    private double calculateHeartRateScore(Double avgHeartRate) {
        if (avgHeartRate == null) return 0;

        if (avgHeartRate >= 120 && avgHeartRate <= 170) {
            return 1.0;
        }

        if (avgHeartRate > 170 && avgHeartRate <= 190) {
            return 0.6;
        }

        if (avgHeartRate > 190) {
            return 0.3;
        }

        return 0.5;
    }

    private double calculateAnalysisBonus(SessionAnalysisResponse analysis) {
        if (analysis == null || analysis.getRiskLevel() == null) {
            return 0.5;
        }

        return switch (analysis.getRiskLevel()) {
            case "LOW_RISK" -> 1.0;
            case "MODERATE_RISK" -> 0.6;
            case "HIGH_RISK" -> 0.2;
            default -> 0.5;
        };
    }

    private double normalize(Double value, double min, double max) {
        if (value == null) return 0;

        if (max == min) return 0;

        return clamp((value - min) / (max - min), 0, 1);
    }

    private double normalize(Integer value, double min, double max) {
        if (value == null) return 0;

        return normalize(value.doubleValue(), min, max);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}