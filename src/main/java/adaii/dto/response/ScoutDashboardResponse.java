package adaii.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScoutDashboardResponse {

    private Integer playersCount;
    private Integer watchlistCount;
    private Integer highPotentialCount;

    private List<ScoutPlayerResponse> latestWatchlistPlayers;
}