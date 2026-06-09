package adaii.service;

import adaii.dto.request.SensorDataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttSensorDataSubscriber {

    private final ObjectMapper objectMapper;
    private final SensorDataService sensorDataService;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<String> message) {
        try {
            String topic = message.getHeaders()
                    .get("mqtt_receivedTopic", String.class);
            String payload = message.getPayload();

            log.info("[MQTT] Message received on topic: {}", topic);
            log.debug("[MQTT] Payload: {}", payload);

            SensorDataRequest request =
                    objectMapper.readValue(payload, SensorDataRequest.class);

            enrichFromTopic(topic, request);

            log.info("[MQTT] Device UUID resolved: {}", request.getDeviceUuid());
            System.out.println("[MQTT DEBUG] Looking up deviceUuid: '" + request.getDeviceUuid() + "'");

            sensorDataService.ingest(request);

            log.info("[MQTT] Sensor data saved successfully from topic: {}", topic);

        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.error("[MQTT] Failed to parse payload: {}", ex.getMessage());
        } catch (adaii.exception.InvalidSessionStateException ex) {
            log.warn("[MQTT] No active session for device — data dropped: {}", ex.getMessage());
        } catch (RuntimeException ex) {
            log.error("[MQTT] Failed to ingest sensor data: {}", ex.getMessage(), ex);
        } catch (Exception ex) {
            System.out.println("[MQTT ERROR] " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void enrichFromTopic(String topic, SensorDataRequest request) {
        if (topic == null) {
            log.warn("[MQTT] Topic is null, cannot extract deviceUuid");
            return;
        }
        // expected: devices/{deviceUuid}/data
        String[] parts = topic.split("/");
        if (parts.length >= 3) {
            request.setDeviceUuid(parts[1]);
            log.debug("[MQTT] Extracted deviceUuid from topic: {}", parts[1]);
        } else {
            log.warn("[MQTT] Unexpected topic format: {}", topic);
        }
    }
}