package org.example.freighthandler.alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private DestinationAlertRepository alertRepository;

    @GetMapping("/active")
    public ResponseEntity<List<DestinationAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertRepository.findByActiveTrue());
    }

    @GetMapping("/destination/{destination}")
    public ResponseEntity<DestinationAlert> getAlertForDestination(
            @PathVariable String destination) {
        return alertRepository.findByDestinationAndActiveTrue(destination)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
