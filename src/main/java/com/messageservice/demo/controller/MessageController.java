/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.messageservice.demo.controller;

import com.messageservice.demo.model.Device;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pipes
 */
@RestController
public class MessageController {

    private Map<String, String[]> messageStore = new HashMap<>();

    @PostMapping("/message")
    public ResponseEntity<?> processMessage(@RequestBody List<Device> devices) {
        StringBuilder finalMessage = new StringBuilder();

        for (Device device : devices) {
            String[] message = device.getMessage();
            StringBuilder processedMessage = new StringBuilder();

            for (String word : message) {
                if (word.isEmpty()) {
                    processedMessage.append(" ");
                } else {
                    processedMessage.append(word).append(" ");
                }
            }

            finalMessage.append(processedMessage.toString().trim()).append(" ");
        }

        String responseMessage = finalMessage.toString().trim();
        if (responseMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo determinar el mensaje");
        } else {
            return ResponseEntity.ok().body(responseMessage);
        }
    }

    @PostMapping("/message_split/{device_id}")
    public ResponseEntity<?> processMessageSplit(@PathVariable("device_id") String deviceId, @RequestBody Device device) {
        messageStore.put(deviceId, device.getMessage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/message_split/{device_id}")
    public ResponseEntity<?> getMessageSplit(@PathVariable("device_id") String deviceId) {
        String[] message = messageStore.get(deviceId);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo determinar el mensaje para el dispositivo con ID: " + deviceId);
        }

        StringBuilder processedMessage = new StringBuilder();
        for (String word : message) {
            if (word.isEmpty()) {
                processedMessage.append(" ");
            } else {
                processedMessage.append(word).append(" ");
            }
        }

        String responseMessage = processedMessage.toString().trim();
        if (responseMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo determinar el mensaje para el dispositivo con ID: " + deviceId);
        } else {
            return ResponseEntity.ok().body(responseMessage);
        }
    }
}
