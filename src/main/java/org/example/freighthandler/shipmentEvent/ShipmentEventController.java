package org.example.freighthandler.shipmentEvent;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class ShipmentEventController {

    @Autowired
    private ShipmentEventService shipmentEventService;

    // 1. GET ALL EVENTS FOR A SHIPMENT (Returns DTOs)
    @GetMapping("/shipment/{shipmentNumber}")
    public ResponseEntity<List<ShipmentEventDTO>> getEventsByShipment(@PathVariable Long shipmentNumber) {
        try{
            List<ShipmentEvent> events = shipmentEventService.getShipmentEventByShipmentNumber(shipmentNumber);

            if(events.isEmpty()){
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(events.stream().map(this::toDto).collect(Collectors.toList()));
        }

        catch( Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. GET ALL EVENTS FOR A USER (Returns DTOs)
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<ShipmentEventDTO>> getEventsByUser(
            @PathVariable Long userID){
        try {
            List<ShipmentEvent> events = shipmentEventService.getShipmentEventByUserID(userID);
            if (events.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(events.stream().map(this::toDto).collect(Collectors.toList()));
        }

            catch( Exception e){
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Helper method to convert ShipmentEvent entity to ShipmentEventDTO
    private ShipmentEventDTO toDto(ShipmentEvent event) {
        ShipmentEventDTO dto = new ShipmentEventDTO();
        dto.setShipmentNumber(event.getShipment().getShipmentNumber());
        dto.setLocation(event.getLocation());
        dto.setPerformedBy(event.getPerformedBy().getName()); // ← was .toString()
        dto.setShipmentEventType(event.getEventType().name()); // ← use .name() not .toString()
        dto.setTrailerNumber(event.getTrailerNumber());         // ← was missing
        dto.setEventTime(event.getEventTime().toString());
        return dto;
    }


}
