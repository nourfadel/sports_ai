package adaii.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CoachDashboardResponse {

    private String teamName;

    private Integer playersCount;
    private Integer activeSessionsCount;
    private Integer unreadAlertsCount;
    private Integer highRiskAnalysesCount;

    private List<CoachPlayerResponse> teamPlayers;
    private List<AlertResponse> latestAlerts;
}