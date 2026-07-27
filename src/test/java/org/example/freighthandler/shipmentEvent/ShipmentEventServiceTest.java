package org.example.freighthandler.shipmentEvent;

import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ShipmentEventService.class)
public class ShipmentEventServiceTest {

    @MockBean
    private ShipmentEventRepository shipmentEventRepository;

    @Autowired
    private ShipmentEventService shipmentEventService;

    private Shipment buildShipment(Long shipmentNumber) {
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(shipmentNumber);
        return shipment;
    }

    private User buildUser(Long userId, String name) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        return user;
    }

    private ShipmentEvent buildEvent(Shipment shipment, User user,
                                     ShipmentEventType type,
                                     String trailerNumber, String location) {
        ShipmentEvent event = new ShipmentEvent(
                shipment, user, type, trailerNumber, location
        );
        event.setEventTime(LocalDateTime.now());
        return event;
    }

    @Test
    public void testSaveShipmentEvent_created_returnsEvent() {
        Shipment shipment = buildShipment(111L);
        User author = buildUser(1001L, "John Doe");
        ShipmentEvent event = buildEvent(
                shipment, author, ShipmentEventType.CREATED, null, "Door 244"
        );

        Mockito.when(shipmentEventRepository.save(Mockito.any(ShipmentEvent.class)))
                .thenReturn(event);

        ShipmentEvent result = shipmentEventService.saveShipmentEvent(
                shipment, author, ShipmentEventType.CREATED, null, "Door 244"
        );

        assertNotNull(result);
        assertEquals(ShipmentEventType.CREATED, result.getEventType());
        assertEquals("Door 244", result.getLocation());
        assertNull(result.getTrailerNumber());
    }

    @Test
    public void testSaveShipmentEvent_load_returnsEvent() {
        Shipment shipment = buildShipment(111L);
        User author = buildUser(1001L, "John Doe");
        ShipmentEvent event = buildEvent(
                shipment, author, ShipmentEventType.LOAD, "P344", "Door 12"
        );

        Mockito.when(shipmentEventRepository.save(Mockito.any(ShipmentEvent.class)))
                .thenReturn(event);

        ShipmentEvent result = shipmentEventService.saveShipmentEvent(
                shipment, author, ShipmentEventType.LOAD, "P344", "Door 12"
        );

        assertNotNull(result);
        assertEquals(ShipmentEventType.LOAD, result.getEventType());
        assertEquals("P344", result.getTrailerNumber());
        assertEquals("Door 12", result.getLocation());
    }

    @Test
    public void testSaveShipmentEvent_unload_returnsEvent() {
        Shipment shipment = buildShipment(111L);
        User author = buildUser(1001L, "John Doe");
        ShipmentEvent event = buildEvent(
                shipment, author, ShipmentEventType.UNLOAD, null, "Bay 5"
        );

        Mockito.when(shipmentEventRepository.save(Mockito.any(ShipmentEvent.class)))
                .thenReturn(event);

        ShipmentEvent result = shipmentEventService.saveShipmentEvent(
                shipment, author, ShipmentEventType.UNLOAD, null, "Bay 5"
        );

        assertNotNull(result);
        assertEquals(ShipmentEventType.UNLOAD, result.getEventType());
        assertNull(result.getTrailerNumber());
        assertEquals("Bay 5", result.getLocation());
    }

    @Test
    public void testGetShipmentEventByShipmentNumber_returnsList() {
        Shipment shipment = buildShipment(111L);
        User author = buildUser(1001L, "John Doe");

        ShipmentEvent event1 = buildEvent(
                shipment, author, ShipmentEventType.CREATED, null, "Door 244"
        );
        ShipmentEvent event2 = buildEvent(
                shipment, author, ShipmentEventType.LOAD, "P344", "Door 12"
        );

        Mockito.when(shipmentEventRepository.findByShipment_ShipmentNumber(111L))
                .thenReturn(Arrays.asList(event1, event2));

        List<ShipmentEvent> result = shipmentEventService
                .getShipmentEventByShipmentNumber(111L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ShipmentEventType.CREATED, result.get(0).getEventType());
        assertEquals(ShipmentEventType.LOAD, result.get(1).getEventType());
    }

    @Test
    public void testGetShipmentEventByShipmentNumber_noEvents_returnsEmptyList() {
        Mockito.when(shipmentEventRepository.findByShipment_ShipmentNumber(999L))
                .thenReturn(Arrays.asList());

        List<ShipmentEvent> result = shipmentEventService
                .getShipmentEventByShipmentNumber(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetShipmentEventByUserID_returnsList() {
        Shipment shipment = buildShipment(111L);
        User author = buildUser(1001L, "John Doe");

        ShipmentEvent event1 = buildEvent(
                shipment, author, ShipmentEventType.LOAD, "P344", "Door 12"
        );
        ShipmentEvent event2 = buildEvent(
                shipment, author, ShipmentEventType.UNLOAD, null, "Bay 5"
        );

        Mockito.when(shipmentEventRepository.findByPerformedBy_UserId(1001L))
                .thenReturn(Arrays.asList(event1, event2));

        List<ShipmentEvent> result = shipmentEventService
                .getShipmentEventByUserID(1001L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ShipmentEventType.LOAD, result.get(0).getEventType());
        assertEquals(ShipmentEventType.UNLOAD, result.get(1).getEventType());
    }

    @Test
    public void testGetShipmentEventByUserID_noEvents_returnsEmptyList() {
        Mockito.when(shipmentEventRepository.findByPerformedBy_UserId(999L))
                .thenReturn(Arrays.asList());

        List<ShipmentEvent> result = shipmentEventService
                .getShipmentEventByUserID(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}