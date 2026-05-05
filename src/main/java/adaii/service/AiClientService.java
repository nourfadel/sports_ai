package adaii.service;

import adaii.dto.AiAnalysisRequest;
import adaii.dto.AiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AiClientService {

    private final RestClient restClient;

    public AiAnalysisResponse analyzeSession(AiAnalysisRequest request) {
        return restClient.post()
                .uri("/api/ai/analyze-session")
                .body(request)
                .retrieve()
                .body(AiAnalysisResponse.class);
    }
}