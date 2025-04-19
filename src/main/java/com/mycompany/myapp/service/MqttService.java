package com.mycompany.myapp.service;

import com.mycompany.myapp.config.MqttConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MqttService {
    @Autowired
    private MqttConfig.MqttGateway mqttGateway;
//    @Autowired
//    private DataSensorsRepository dataSensorsRepository;
//    @Autowired
//    private ActionHistoryRepository actionHistoryRepository;

    // ConcurrentHashMap để quản lý latch cho từng thiết bị
    private ConcurrentHashMap<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        String payload = message.getPayload();

        if ("information".equals(topic)) {
            System.out.println("check information topic");
        }
    }

//    public boolean controlDevice(String device, String action) {
//        // Tạo tin nhắn điều khiển
//        String message = device + "_" + action;
//        CountDownLatch latch = new CountDownLatch(1); // Tạo latch mới cho thiết bị
//
//        // Lưu latch vào bản đồ
//        latchMap.put(device, latch);
//
//        try {
//            mqttGateway.sendToMqtt(message, "topic/control");
//            System.out.println("Sent control message: " + message);
//
//            // Đợi phản hồi từ thiết bị hoặc timeout sau 10 giây
//            boolean success = latch.await(10, TimeUnit.SECONDS);
//            if (success) {
//                return true;
//            } else {
//                System.out.println("Timeout waiting for action history of " + device);
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


}

