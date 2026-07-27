package org.example.freighthandler.shipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ShipmentService.class)
public class ShipmentServiceTest {

    @MockBean
    private ShipmentRepository shipmentRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private ShipmentService shipmentService;

    @BeforeEach
    public void setup() {
        shipmentService.entityManager = entityManager;
    }

    @Test
    public void testGetShipmentByNumber_returnsShipment() {
        // Arrange
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(888842017113L);
        shipment.setCurrLocation("Door 244");

        Mockito.when(shipmentRepository.findByShipmentNumber(888842017113L))
                .thenReturn(Optional.of(shipment));

        // Act
        Shipment result = shipmentService.getShipmentByNumber(888842017113L);

        // Assert
        assertNotNull(result);
        assertEquals(888842017113L, result.getShipmentNumber());
        assertEquals("Door 244", result.getCurrLocation());
    }

    @Test
    public void testGetShipmentByNumber_notFound_returnsNull() {
        // Arrange
        Mockito.when(shipmentRepository.findByShipmentNumber(999L))
                .thenReturn(Optional.empty());

        // Act
        Shipment result = shipmentService.getShipmentByNumber(999L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetShipmentsByTrailer_returnsList() {
        // Arrange
        Shipment s1 = new Shipment();
        s1.setShipmentNumber(111L);

        Shipment s2 = new Shipment();
        s2.setShipmentNumber(222L);

        Mockito.when(shipmentRepository.findByTrailer_TrailerNumber("T-99"))
                .thenReturn(Arrays.asList(s1, s2));

        // Act
        List<Shipment> result = shipmentService.getShipmentsByTrailer("T-99");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(111L, result.get(0).getShipmentNumber());
        assertEquals(222L, result.get(1).getShipmentNumber());
    }

    @Test
    public void testGetShipmentsByTrailer_noShipments_returnsEmptyList() {
        // Arrange
        Mockito.when(shipmentRepository.findByTrailer_TrailerNumber("T-00"))
                .thenReturn(Arrays.asList());

        // Act
        List<Shipment> result = shipmentService.getShipmentsByTrailer("T-00");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testSaveShipment_returnsSavedShipment() {
        // Arrange
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(111L);
        shipment.setCurrLocation("Door 244");

        Mockito.when(shipmentRepository.saveAndFlush(shipment)).thenReturn(shipment);
        Mockito.when(shipmentRepository.findById(null)).thenReturn(Optional.of(shipment));
        Mockito.doNothing().when(entityManager).flush();
        Mockito.doNothing().when(entityManager).clear();

        // Act
        Shipment result = shipmentService.saveShipment(shipment);

        // Assert
        assertNotNull(result);
        assertEquals(111L, result.getShipmentNumber());
        assertEquals("Door 244", result.getCurrLocation());
    }

    @Test
    public void testDeleteShipment_exists_returnsTrue() {
        // Arrange
        Mockito.when(shipmentRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(shipmentRepository).deleteById(1L);

        // Act
        boolean result = shipmentService.deleteShipment(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDeleteShipment_notFound_returnsFalse() {
        // Arrange
        Mockito.when(shipmentRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = shipmentService.deleteShipment(99L);

        // Assert
        assertFalse(result);
    }
}