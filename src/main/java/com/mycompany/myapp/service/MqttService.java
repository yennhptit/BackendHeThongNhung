package com.mycompany.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mycompany.myapp.config.MqttConfig;
import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.impl.DriverServiceImpl;
import com.mycompany.myapp.service.impl.TripServiceImpl;
import com.mycompany.myapp.service.impl.ViolationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MqttService {
    private MqttConfig.MqttGateway mqttGateway;

    private ObjectMapper objectMapper;

    private DriverServiceImpl driverService;

    private TripServiceImpl tripService;

    private ViolationServiceImpl violationService;

    public MqttService(MqttConfig.MqttGateway mqttGateway,
                       ObjectMapper objectMapper,
                       DriverServiceImpl driverService,
                       TripServiceImpl tripService,
                       ViolationServiceImpl violationService) {
        this.mqttGateway = mqttGateway;
        this.objectMapper = objectMapper;
        this.driverService = driverService;
        this.tripService = tripService;
        this.violationService = violationService;
    }


    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        String payload = message.getPayload();

        switch (topic) {
            case "information/request":
                handleDriverInformation(payload);
                break;
            case "alcohol":
                handleAlcoholDetection(payload);
                break;
            case "drowsiness":
                handleDrowsinessDetection(payload);
                break;
            case "trip":
                handleTripInformation(payload);
                break;
            default:
                System.out.println("Unhandled topic: " + topic);
                break;
        }

    }

    private void handleDriverInformation(String driverId) {
        System.out.println("Payload: " + driverId);
        try {
            Optional<Driver> driver = driverService.findByDriverId(driverId);

            // Tạo ObjectNode cho response
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("driverId", driverId);

            if (driver.isPresent()) {
                ObjectNode dataNode = objectMapper.createObjectNode();
                dataNode.put("imageUrl", driver.get().getFaceData());
                responseNode.set("data", dataNode);
            } else {
                responseNode.putNull("data");
                System.out.println("Driver not found: " + driverId);
            }

            String response = objectMapper.writeValueAsString(responseNode);
            System.out.println("Sent response: " + response);
            mqttGateway.sendToMqtt(response, "information/response");
            System.out.println("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAlcoholDetection(String payload) {
        System.out.println("Alcohol Payload: " + payload);
        try {
            // Parse payload
            JsonNode root = objectMapper.readTree(payload);

            // Lấy driverId
            String driverId = root.path("driverId").asText(null);
            if (driverId == null) {
                System.err.println("Missing driverId in alcohol payload");
                return;
            }

            // Lấy trường data dưới dạng chuỗi
            String dataText = root.path("data").asText(null);
            if (dataText == null) {
                System.err.println("Missing data in trip payload");
                return;
            }

            // Parse chuỗi JSON bên trong data
            JsonNode dataNode = objectMapper.readTree(dataText);

            float value = (float) dataNode.path("value").asDouble(-1.0);

            if (value < 0) {
                System.err.println("Invalid value in alcohol data");
                return;
            }

            // Lưu vi phạm nồng độ cồn
            boolean rs = violationService.saveAlcoholViolation(driverId, value);
            if (rs) {
                System.out.println("Alcohol saved.");
            } else {
                System.err.println("Failed to save alcohol.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDrowsinessDetection(String driverId) {
        System.out.println("Drowsiness Payload: " + driverId);
        try {

            boolean rs = violationService.saveDrowsinessViolation(driverId);
            if (rs) {
                System.out.println("Drowsiness saved.");
            } else {
                System.err.println("Failed to save drowsiness.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTripInformation(String payload) {
        System.out.println("Trip Payload: " + payload);
        try {
            // 1. Parse payload chính
            JsonNode root = objectMapper.readTree(payload);

            // 2. Lấy driverId
            String driverId = root.path("driverId").asText(null);
            if (driverId == null) {
                System.err.println("Missing driverId in trip payload");
                return;
            }

            // 3. Lấy trường data dưới dạng chuỗi
            String dataText = root.path("data").asText(null);
            if (dataText == null) {
                System.err.println("Missing data in trip payload");
                return;
            }

            // 4. Parse chuỗi JSON bên trong data
            JsonNode dataNode = objectMapper.readTree(dataText);

            boolean running = dataNode.path("running").asBoolean(false);
            Long vehicleId = dataNode.path("vehicleId").asLong(-1);

            if (vehicleId < 0) {
                System.err.println("Invalid vehicleId in trip data");
                return;
            }

            // 5. Xử lý lưu chuyến đi
            boolean rs = tripService.saveTrip(driverId, vehicleId, running);
            if (rs) {
                System.out.println("Trip saved.");
            } else {
                System.err.println("Failed to save trip.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

