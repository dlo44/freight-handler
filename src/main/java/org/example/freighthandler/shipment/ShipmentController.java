package org.example.freighthandler.shipment;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.freighthandler.destination.Destination;
import org.example.freighthandler.shipmentEvent.ShipmentEvent;
import org.example.freighthandler.shipmentEvent.ShipmentEventService;
import org.example.freighthandler.shipmentEvent.ShipmentEventType;
import org.example.freighthandler.trailer.Trailer;
import org.example.freighthandler.trailer.TrailerService;
import org.example.freighthandler.user.User;
import org.example.freighthandler.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private TrailerService trailerService;

    @Autowired
    private ShipmentEventService shipmentEventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. GET ALL SHIPMENTS FOR A TRAILER (Returns DTOs)
    @GetMapping("/trailer/{trailerNumber}")
    public ResponseEntity<List<ShipmentDTO>> getShipmentsByTrailer(@PathVariable String trailerNumber) {
        try {
            List<Shipment> shipments = shipmentService.getShipmentsByTrailer(trailerNumber);

            if (shipments.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Convert List<Entity> to List<DTO>
            List<ShipmentDTO> dtos = shipments.stream()
                    .map(shipment -> modelMapper.map(shipment, ShipmentDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. GET SINGLE SHIPMENT BY PRO NUMBER (Returns DTO)
    @GetMapping("/{shipmentNumber}")
    public ResponseEntity<ShipmentDTO> getShipmentByNumber(@PathVariable Long shipmentNumber) {
        try {
            Shipment shipment = shipmentService.getShipmentByNumber(shipmentNumber);
            if (shipment == null) {
                return ResponseEntity.notFound().build();
            }

            ShipmentDTO dto = modelMapper.map(shipment, ShipmentDTO.class);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. CREATE NEW SHIPMENT
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody ShipmentRequestDTO shipmentRequest) {
        try {

            User author = userService.findByUserIdNumber(shipmentRequest.getAuthorId());
            Shipment shipment = new Shipment();
            shipment.setShipmentNumber(shipmentRequest.getShipmentNumber());
            shipment.setCurrLocation(shipmentRequest.getCurrLocation());
            shipment.setReceiverAddress(shipmentRequest.getReceiverAddress());
            shipment.setShipperAddress(shipmentRequest.getShipperAddress());
            Trailer trailer = trailerService.getTrailerByNumber(shipmentRequest.getTrailerNumber());
            shipment.setTrailer(trailer);
            shipment.setDestination(Destination.valueOf(shipmentRequest.getDestination()));


            Shipment savedShipment = shipmentService.saveShipment(shipment);

            shipmentEventService.saveShipmentEvent(savedShipment, author, ShipmentEventType.CREATED, shipmentRequest.getTrailerNumber(), shipmentRequest.getCurrLocation());

            ShipmentDTO dto = modelMapper.map(savedShipment, ShipmentDTO.class);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    // 4. UPDATE SHIPMENT (Scanning to trailer or unloading to bay)
    @PutMapping("/{shipmentNumber}")
    public ResponseEntity<ShipmentDTO> updateShipment(@PathVariable Long shipmentNumber,
                                                      @RequestBody ShipmentUpdateRequestDTO shipmentUpdate) {
        try {
            Shipment existingShipment = shipmentService.getShipmentByNumber(shipmentNumber);
            if (existingShipment == null) {
                return ResponseEntity.notFound().build();
            }

            if (shipmentUpdate.getCurrLocation() != null) {
                existingShipment.setCurrLocation(shipmentUpdate.getCurrLocation());
            }

            if (shipmentUpdate.getTrailerNumber() != null) {
                Trailer foundTrailer = trailerService.getTrailerByNumber(shipmentUpdate.getTrailerNumber());
                if (foundTrailer != null) {
                    existingShipment.setTrailer(foundTrailer);
                } else {
                    return ResponseEntity.notFound().build(); // ← just this changes
                }
            } else {
                existingShipment.setTrailer(null);
            }

            Shipment savedShipment = shipmentService.saveShipment(existingShipment);

            shipmentEventService.saveShipmentEvent(
                    savedShipment,
                    userService.findByUserIdNumber(shipmentUpdate.getAuthorId()),
                    ShipmentEventType.valueOf(shipmentUpdate.getEventType()),
                    shipmentUpdate.getTrailerNumber(),
                    shipmentUpdate.getCurrLocation()
            );
            return ResponseEntity.ok(modelMapper.map(savedShipment, ShipmentDTO.class));

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 5. DELETE SHIPMENT
    @DeleteMapping(path = "/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long shipmentId) {
        try {
            return shipmentService.deleteShipment(shipmentId)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}