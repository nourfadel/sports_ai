package adaii.service;

import adaii.dto.HardwareSensorDataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

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

            HardwareSensorDataRequest request =
                    objectMapper.readValue(payload, HardwareSensorDataRequest.class);

            enrichFromTopic(topic, request);

            sensorDataService.ingest(request);

            System.out.println("[MQTT] Sensor data saved from topic: " + topic);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void enrichFromTopic(String topic, HardwareSensorDataRequest request) {
        // expected: devices/{deviceUuid}/data
        String[] parts = topic.split("/");

        if (parts.length >= 3) {
            request.setDeviceUuid(parts[1]);
        }

    }

}
