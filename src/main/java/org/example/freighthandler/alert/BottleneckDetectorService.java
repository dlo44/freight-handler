package org.example.freighthandler.alert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.shipment.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BottleneckDetectorService {

    @Autowired
    private DestinationAlertRepository alertRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;  // ← add this

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, List<Instant>> pendingUnloads = new ConcurrentHashMap<>();
    private static final long ALERT_THRESHOLD_MINUTES = 2;

    @KafkaListener(
            topics = "freightdb.public.shipment_events",
            groupId = "freight-monitor"
    )
    public void processEvent(String message) {
        System.out.println("=== KAFKA MESSAGE RECEIVED ===");
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode payload = root.get("payload");

            if (payload == null || payload.isNull()) {
                System.out.println("=== PAYLOAD NULL ===");
                return;
            }

            JsonNode after = payload.get("after");
            if (after == null || after.isNull()) {
                System.out.println("=== AFTER NULL ===");
                return;
            }

            String eventType = after.get("event_type").asText();
            long shipmentId = after.get("shipment_id").asLong();
            System.out.println("=== EVENT: " + eventType + " SHIPMENT: " + shipmentId + " ===");

            // Look up destination from shipment table
            Shipment shipment = shipmentRepository.findById(shipmentId).orElse(null);
            if (shipment == null) {
                System.out.println("=== SHIPMENT NOT FOUND: " + shipmentId + " ===");
                return;
            }

            String destination = shipment.getDestination().name();
            System.out.println("=== DESTINATION: " + destination + " ===");
            Instant eventTime = Instant.now();

            if ("UNLOAD".equals(eventType)) {
                pendingUnloads
                        .computeIfAbsent(destination, k -> new ArrayList<>())
                        .add(eventTime);
                System.out.println("=== PENDING UNLOADS FOR " + destination +
                        ": " + pendingUnloads.get(destination).size() + " ===");

            } else if ("LOAD".equals(eventType)) {
                List<Instant> pending = pendingUnloads.get(destination);
                if (pending != null && !pending.isEmpty()) {
                    Instant unloadTime = pending.remove(0);
                    long dwellMinutes = Duration.between(unloadTime, eventTime).toMinutes();
                    System.out.println("=== DWELL: " + dwellMinutes + " mins ===");
                    evaluateBottleneck(destination, dwellMinutes);
                }
            }

        } catch (Exception e) {
            System.out.println("=== ERROR: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }

    private void evaluateBottleneck(String destination, long dwellMinutes) {
        if (dwellMinutes > ALERT_THRESHOLD_MINUTES) {
            DestinationAlert alert = alertRepository
                    .findByDestinationAndActiveTrue(destination)
                    .orElse(new DestinationAlert());

            alert.setDestination(destination);
            alert.setAvgDwellMinutes(dwellMinutes);
            alert.setActive(true);
            alert.setCreatedAt(Instant.now());
            alertRepository.save(alert);

            System.out.println("BOTTLENECK ALERT SAVED: " + destination +
                    " avg dwell " + dwellMinutes + " mins");

        } else {
            alertRepository.findByDestinationAndActiveTrue(destination)
                    .ifPresent(alert -> {
                        alert.setActive(false);
                        alertRepository.save(alert);
                    });
        }
    }
}