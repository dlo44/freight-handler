package org.example.freighthandler.osd;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/osd")
public class OsdEntryController {

    @Autowired
    private OsdService osdService;

    @PostMapping
    public ResponseEntity<OsdEntryDTO> createOsdEntry(@RequestBody OsdEntryDTO osdEntryDTO) {
        try {
            OsdEntry savedEntry = osdService.saveOsdEntry(osdEntryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedEntry));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{shipmentNumber}")
    public ResponseEntity<List<OsdEntryDTO>> getOsdEntries(@PathVariable Long shipmentNumber) {
        try {
            List<OsdEntry> entries = osdService.getOsdEntriesByShipmentNumber(shipmentNumber);

            if (entries.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            List<OsdEntryDTO> response = entries.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private OsdEntryDTO toDto(OsdEntry entry) {
        OsdEntryDTO dto = new OsdEntryDTO();
        dto.setId(entry.getId());
        dto.setQty(entry.getQty());
        dto.setType(entry.getType().name());
        dto.setDamageType(entry.getDamageType() != null ? entry.getDamageType().name() : null);
        dto.setDetails(entry.getDetails());
        dto.setCreatedAt(entry.getCreatedAt().toString());
        dto.setShipmentNumber(entry.getShipment().getShipmentNumber());
        dto.setAuthorId(entry.getAuthor().getUserId());
        return dto;
    }
}
