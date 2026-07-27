package org.example.freighthandler.shipmentEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShipmentEventController.class)
public class ShipmentEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentEventService shipmentEventService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShipmentEvent buildEvent(Long shipmentNumber, Long userId,
                                     String userName, ShipmentEventType type,
                                     String trailerNumber, String location) {
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(shipmentNumber);

        User author = new User();
        author.setUserId(userId);
        author.setName(userName);

        ShipmentEvent event = new ShipmentEvent(
                shipment, author, type, trailerNumber, location
        );
        event.setEventTime(LocalDateTime.now());
        return event;
    }

    // 1. GET BY SHIPMENT NUMBER - happy path
    @Test
    void getShipmentEventByShipmentNumber_returnsListOfDtos() throws Exception {
        ShipmentEvent event1 = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.CREATED, null, "Door 244"
        );
        ShipmentEvent event2 = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.LOAD, "P344", "Door 12"
        );

        when(shipmentEventService.getShipmentEventByShipmentNumber(111L))
                .thenReturn(Arrays.asList(event1, event2));

        mockMvc.perform(get("/api/events/shipment/111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shipmentEventType").value("CREATED"))
                .andExpect(jsonPath("$[0].location").value("Door 244"))
                .andExpect(jsonPath("$[0].performedBy").value("John Doe"))
                .andExpect(jsonPath("$[0].shipmentNumber").value(111))
                .andExpect(jsonPath("$[0].trailerNumber").isEmpty())
                .andExpect(jsonPath("$[1].shipmentEventType").value("LOAD"))
                .andExpect(jsonPath("$[1].trailerNumber").value("P344"))
                .andExpect(jsonPath("$[1].location").value("Door 12"));
    }

    // 2. GET BY SHIPMENT NUMBER - no events
    @Test
    void getShipmentEventByShipmentNumber_noEvents_returnsNoContent() throws Exception {
        when(shipmentEventService.getShipmentEventByShipmentNumber(999L))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/events/shipment/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // 3. GET BY SHIPMENT NUMBER - verify all three event types
    @Test
    void getShipmentEventByShipmentNumber_allEventTypes_returnsCorrectTypes() throws Exception {
        ShipmentEvent created = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.CREATED, null, "Door 244"
        );
        ShipmentEvent load = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.LOAD, "P344", "Door 12"
        );
        ShipmentEvent unload = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.UNLOAD, null, "Bay 5"
        );

        when(shipmentEventService.getShipmentEventByShipmentNumber(111L))
                .thenReturn(Arrays.asList(created, load, unload));

        mockMvc.perform(get("/api/events/shipment/111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].shipmentEventType").value("CREATED"))
                .andExpect(jsonPath("$[1].shipmentEventType").value("LOAD"))
                .andExpect(jsonPath("$[2].shipmentEventType").value("UNLOAD"));
    }

    // 4. GET BY USER ID - happy path
    @Test
    void getShipmentEventByUserID_returnsListOfDtos() throws Exception {
        ShipmentEvent event1 = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.LOAD, "P344", "Door 12"
        );
        ShipmentEvent event2 = buildEvent(
                222L, 1001L, "John Doe",
                ShipmentEventType.UNLOAD, null, "Bay 5"
        );

        when(shipmentEventService.getShipmentEventByUserID(1001L))
                .thenReturn(Arrays.asList(event1, event2));

        mockMvc.perform(get("/api/events/user/1001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shipmentEventType").value("LOAD"))
                .andExpect(jsonPath("$[0].trailerNumber").value("P344"))
                .andExpect(jsonPath("$[0].performedBy").value("John Doe"))
                .andExpect(jsonPath("$[0].shipmentNumber").value(111))
                .andExpect(jsonPath("$[1].shipmentEventType").value("UNLOAD"))
                .andExpect(jsonPath("$[1].location").value("Bay 5"))
                .andExpect(jsonPath("$[1].shipmentNumber").value(222));
    }

    // 5. GET BY USER ID - no events
    @Test
    void getShipmentEventByUserID_noEvents_returnsNoContent() throws Exception {
        when(shipmentEventService.getShipmentEventByUserID(999L))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/events/user/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // 6. GET BY USER ID - multiple shipments for same user
    @Test
    void getShipmentEventByUserID_multipleShipments_returnsAll() throws Exception {
        ShipmentEvent event1 = buildEvent(
                111L, 1001L, "John Doe",
                ShipmentEventType.CREATED, null, "Door 244"
        );
        ShipmentEvent event2 = buildEvent(
                222L, 1001L, "John Doe",
                ShipmentEventType.LOAD, "P455", "Door 5"
        );
        ShipmentEvent event3 = buildEvent(
                333L, 1001L, "John Doe",
                ShipmentEventType.UNLOAD, null, "Bay 3"
        );

        when(shipmentEventService.getShipmentEventByUserID(1001L))
                .thenReturn(Arrays.asList(event1, event2, event3));

        mockMvc.perform(get("/api/events/user/1001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].shipmentNumber").value(111))
                .andExpect(jsonPath("$[1].shipmentNumber").value(222))
                .andExpect(jsonPath("$[2].shipmentNumber").value(333))
                .andExpect(jsonPath("$[0].performedBy").value("John Doe"))
                .andExpect(jsonPath("$[1].performedBy").value("John Doe"))
                .andExpect(jsonPath("$[2].performedBy").value("John Doe"));
    }
}