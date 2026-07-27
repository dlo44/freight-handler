package org.example.freighthandler.trailer;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path= "/api/trailers")
public class TrailerController {
    @Autowired
    private TrailerService trailerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{trailerNumber}", produces = "application/json")
    public ResponseEntity<TrailerDTO> getTrailerByNumber(@PathVariable String trailerNumber) {
        try {
            Trailer trailer = trailerService.getTrailerByNumber(trailerNumber);
            if (trailer == null) return ResponseEntity.notFound().build();

            // Map Entity -> DTO
            TrailerDTO dto = modelMapper.map(trailer, TrailerDTO.class);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "door/{doorNumber}", produces = "application/json")
    public ResponseEntity<TrailerDTO> getTrailerByDoorNumber(@PathVariable int doorNumber) {
        Trailer trailer = trailerService.findByDoorNumber(doorNumber);
        if (trailer == null) return ResponseEntity.notFound().build();

        // Map Entity -> DTO
        TrailerDTO dto = modelMapper.map(trailer, TrailerDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<TrailerDTO> createTrailer(@RequestBody Trailer trailer) {
        try {
            Trailer savedTrailer = trailerService.saveTrailer(trailer);

            // Return DTO to keep JSON consistent
            TrailerDTO dto = modelMapper.map(savedTrailer, TrailerDTO.class);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}
