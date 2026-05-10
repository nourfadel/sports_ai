package adaii.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoutPlayerComparisonResponse {

    private ScoutPlayerDetailsResponse player1;
    private ScoutPlayerDetailsResponse player2;
}