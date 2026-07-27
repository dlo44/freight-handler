package org.example.freighthandler.door;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.freighthandler.trailer.Trailer;
import org.example.freighthandler.trailer.TrailerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/doors")
public class DoorController {

    @Autowired
    private DoorService doorService;

    @Autowired
    private TrailerService trailerService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. GET a specific door by number (Returns DTO)
    @GetMapping("/{doorNumber}")
    public ResponseEntity<DoorDTO> getDoorInfo(@PathVariable int doorNumber) {
        Door door = doorService.getDoorInfo(doorNumber);
        if (door != null) {
            return ResponseEntity.ok(modelMapper.map(door, DoorDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 2. GET all doors (Returns List of DTOs)
    @GetMapping
    public ResponseEntity<List<DoorDTO>> getAllDoors() {
        try {
            List<Door> doors = doorService.getAllDoors(); // make sure DoorService has this method
            List<DoorDTO> dtos = doors.stream()
                    .map(door -> modelMapper.map(door, DoorDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // 3. POST to create a new door
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<DoorDTO> createDoor(@RequestBody DoorUpdateRequestDTO request) {
        try {
            Door door = new Door();
            door.setDoorNumber(request.getDoorNumber());
            door.setStatus(DoorStatus.valueOf(request.getStatus()));

            if(request.getTrailerNumber() != null) {
                Trailer trailer = trailerService.getTrailerByNumber(request.getTrailerNumber());
                if(trailer == null) {
                    return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
                }
                door.setTrailer(trailer);
            }

            Door savedDoor = doorService.saveDoor(door);

            return new ResponseEntity<>(modelMapper.map(savedDoor, DoorDTO.class), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    // 4. PUT to update a door assignment
    @PutMapping("/{doorNumber}")
    public ResponseEntity<?> updateDoorAssignment(@PathVariable int doorNumber, @RequestBody Door doorUpdate) {
        try {
            Door existingDoor = doorService.getDoorInfo(doorNumber);
            if (existingDoor == null) {
                return ResponseEntity.notFound().build();
            }

            if (doorUpdate.getStatus() != null) {
                existingDoor.setStatus(doorUpdate.getStatus());
            }

            // Assign or Unassign Trailer
            if (doorUpdate.getTrailer() != null && doorUpdate.getTrailer().getTrailerNumber() != null) {
                String trailerNumber = doorUpdate.getTrailer().getTrailerNumber();
                Trailer foundTrailer = trailerService.getTrailerByNumber(trailerNumber);

                if (foundTrailer != null) {
                    existingDoor.setTrailer(foundTrailer);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Trailer " + trailerNumber + " does not exist.");
                }
            } else {
                existingDoor.setTrailer(null);
            }

            Door savedDoor = doorService.saveDoor(existingDoor);
            return ResponseEntity.ok(modelMapper.map(savedDoor, DoorDTO.class));

        } catch (Exception e) {
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}