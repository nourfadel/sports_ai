package adaii.controller;

import adaii.dto.ApiResponse;
import adaii.dto.LiveSensorDataResponse;
import adaii.dto.SensorDataResponse;
import adaii.dto.SessionSummaryResponse;
import adaii.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @GetMapping("/{sessionId}/live")
    public ResponseEntity<ApiResponse<LiveSensorDataResponse>> getLiveData(
            @PathVariable Long sessionId
    ) {
        LiveSensorDataResponse response = sensorDataService.getLiveData(sessionId);

        return ResponseEntity.ok(
                ApiResponse.<LiveSensorDataResponse>builder()
                        .status("SUCCESS")
                        .message("Live sensor data fetched successfully")
                        .data(response)
                        .build()
        );
    }


    @GetMapping("/{sessionId}/sensor-data")
    public ResponseEntity<ApiResponse<List<SensorDataResponse>>> getSessionData(@PathVariable Long sessionId){
        return ResponseEntity.ok(
                ApiResponse.<List<SensorDataResponse>>builder()
                        .status("SUCCESS")
                        .message("Session sensor data fetched")
                        .data(sensorDataService.getSessionData(sessionId))
                        .build()
        );
    }


    @GetMapping("/{sessionId}/summary")
    public ResponseEntity<ApiResponse<SessionSummaryResponse>> getSessionSummary(
            @PathVariable Long sessionId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<SessionSummaryResponse>builder()
                        .status("SUCCESS")
                        .message("Session summary fetched successfully")
                        .data(sensorDataService.getSessionSummary(sessionId))
                        .build()
        );
    }




}














