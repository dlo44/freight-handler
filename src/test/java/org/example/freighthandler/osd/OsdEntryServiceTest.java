package org.example.freighthandler.osd;

import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.shipment.ShipmentRepository;
import org.example.freighthandler.user.User;
import org.example.freighthandler.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OsdService.class)
public class OsdEntryServiceTest {

    @MockBean
    private OsdRepository osdRepository;

    @MockBean
    private ShipmentRepository shipmentRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private OsdService osdService;

    @Test
    public void testSaveOsdEntry_returnsSavedEntry() {
        // Arrange
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(111L);

        User author = new User();
        author.setUserId(1L);

        OsdEntryDTO dto = new OsdEntryDTO();
        dto.setShipmentNumber(111L);
        dto.setAuthorId(1L);
        dto.setType("DAMAGED");
        dto.setDamageType("CRUSHED");
        dto.setQty(2);
        dto.setDetails("Box was crushed on arrival");

        OsdEntry savedEntry = new OsdEntry(
                2, OsdType.DAMAGED, OsdDamageType.CRUSHED,
                "Box was crushed on arrival", author, shipment
        );
        savedEntry.setCreatedAt(LocalDateTime.now());

        Mockito.when(shipmentRepository.findByShipmentNumber(111L))
                .thenReturn(Optional.of(shipment));
        Mockito.when(userRepository.findByUserId(1L))
                .thenReturn(Optional.of(author));
        Mockito.when(osdRepository.save(Mockito.any(OsdEntry.class)))
                .thenReturn(savedEntry);

        // Act
        OsdEntry result = osdService.saveOsdEntry(dto);

        // Assert
        assertNotNull(result);
        assertEquals(OsdType.DAMAGED, result.getType());
        assertEquals(OsdDamageType.CRUSHED, result.getDamageType());
        assertEquals(2, result.getQty());
        assertEquals("Box was crushed on arrival", result.getDetails());
    }

    @Test
    public void testSaveOsdEntry_shipmentNotFound_throwsException() {
        // Arrange
        OsdEntryDTO dto = new OsdEntryDTO();
        dto.setShipmentNumber(999L);
        dto.setAuthorId(1L);
        dto.setType("SHORT");

        Mockito.when(shipmentRepository.findByShipmentNumber(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> osdService.saveOsdEntry(dto));
    }

    @Test
    public void testSaveOsdEntry_userNotFound_throwsException() {
        // Arrange
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(111L);

        OsdEntryDTO dto = new OsdEntryDTO();
        dto.setShipmentNumber(111L);
        dto.setAuthorId(999L);
        dto.setType("SHORT");

        Mockito.when(shipmentRepository.findByShipmentNumber(111L))
                .thenReturn(Optional.of(shipment));
        Mockito.when(userRepository.findByUserId(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> osdService.saveOsdEntry(dto));
    }

    @Test
    public void testGetOsdEntriesByShipmentNumber_returnsList() {
        // Arrange
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(111L);

        User author = new User();
        author.setUserId(1L);

        OsdEntry entry1 = new OsdEntry(
                1, OsdType.SHORT, null, "Missing one box", author, shipment
        );
        OsdEntry entry2 = new OsdEntry(
                2, OsdType.DAMAGED, OsdDamageType.TORN, "Torn packaging", author, shipment
        );

        Mockito.when(osdRepository.findByShipmentShipmentNumber(111L))
                .thenReturn(Arrays.asList(entry1, entry2));

        // Act
        List<OsdEntry> result = osdService.getOsdEntriesByShipmentNumber(111L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(OsdType.SHORT, result.get(0).getType());
        assertEquals(OsdType.DAMAGED, result.get(1).getType());
    }

    @Test
    public void testGetOsdEntriesByShipmentNumber_noEntries_returnsEmptyList() {
        // Arrange
        Mockito.when(osdRepository.findByShipmentShipmentNumber(999L))
                .thenReturn(Arrays.asList());

        // Act
        List<OsdEntry> result = osdService.getOsdEntriesByShipmentNumber(999L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
