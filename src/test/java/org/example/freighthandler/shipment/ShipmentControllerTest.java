package org.example.freighthandler.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freighthandler.destination.Destination;
import org.example.freighthandler.trailer.Trailer;
import org.example.freighthandler.trailer.TrailerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @MockBean
    private TrailerService trailerService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. GET shipments by trailer
    @Test
    void getShipmentsByTrailer_returnsListOfDtos() throws Exception {
        Shipment s1 = new Shipment();
        s1.setShipmentNumber(111L);

        Shipment s2 = new Shipment();
        s2.setShipmentNumber(222L);

        ShipmentDTO dto1 = new ShipmentDTO();
        dto1.setShipmentNumber(111L);

        ShipmentDTO dto2 = new ShipmentDTO();
        dto2.setShipmentNumber(222L);

        when(shipmentService.getShipmentsByTrailer("T-99")).thenReturn(Arrays.asList(s1, s2));
        when(modelMapper.map(eq(s1), eq(ShipmentDTO.class))).thenReturn(dto1);
        when(modelMapper.map(eq(s2), eq(ShipmentDTO.class))).thenReturn(dto2);

        mockMvc.perform(get("/api/shipments/trailer/T-99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shipmentNumber").value(111))
                .andExpect(jsonPath("$[1].shipmentNumber").value(222));
    }

    @Test
    void getShipmentsByTrailer_empty_returnsNoContent() throws Exception {
        when(shipmentService.getShipmentsByTrailer("T-00")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/shipments/trailer/T-00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // 2. GET single shipment by number
    @Test
    void getShipmentByNumber_returnsDto() throws Exception {
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(888842017113L);

        ShipmentDTO dto = new ShipmentDTO();
        dto.setShipmentNumber(888842017113L);
        dto.setCurrLocation("Door 244");

        when(shipmentService.getShipmentByNumber(888842017113L)).thenReturn(shipment);
        when(modelMapper.map(eq(shipment), eq(ShipmentDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/api/shipments/888842017113")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentNumber").value(888842017113L))
                .andExpect(jsonPath("$.currLocation").value("Door 244"));
    }

    @Test
    void getShipmentByNumber_notFound_returns404() throws Exception {
        when(shipmentService.getShipmentByNumber(999L)).thenReturn(null);

        mockMvc.perform(get("/api/shipments/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // 3. POST create shipment
    @Test
    void createShipment_returnsCreatedDto() throws Exception {
        ShipmentRequestDTO request = new ShipmentRequestDTO();
        request.setShipmentNumber(888842017113L);
        request.setDestination("LAX");
        request.setShipperAddress("123 Main St");
        request.setReceiverAddress("456 Oak Ave");
        request.setCurrLocation("Door 244");

        Shipment saved = new Shipment();
        saved.setShipmentNumber(888842017113L);
        saved.setDestination(Destination.LAX);
        saved.setCurrLocation("Door 244");

        ShipmentDTO dto = new ShipmentDTO();
        dto.setShipmentNumber(888842017113L);
        dto.setDestination("LAX");
        dto.setCurrLocation("Door 244");

        when(trailerService.getTrailerByNumber(null)).thenReturn(null);
        when(shipmentService.saveShipment(Mockito.any(Shipment.class))).thenReturn(saved);
        when(modelMapper.map(eq(saved), eq(ShipmentDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shipmentNumber").value(888842017113L))
                .andExpect(jsonPath("$.destination").value("LAX"))
                .andExpect(jsonPath("$.currLocation").value("Door 244"));

        verify(shipmentService).saveShipment(Mockito.any(Shipment.class));
    }

    // 4. PUT update shipment
    @Test
    void updateShipment_updatesLocation_returnsDto() throws Exception {
        ShipmentUpdateRequestDTO request = new ShipmentUpdateRequestDTO();
        request.setCurrLocation("Door 10");

        Shipment existing = new Shipment();
        existing.setShipmentNumber(111L);
        existing.setCurrLocation("Door 244");

        Shipment saved = new Shipment();
        saved.setShipmentNumber(111L);
        saved.setCurrLocation("Door 10");

        ShipmentDTO dto = new ShipmentDTO();
        dto.setShipmentNumber(111L);
        dto.setCurrLocation("Door 10");

        when(shipmentService.getShipmentByNumber(111L)).thenReturn(existing);
        when(shipmentService.saveShipment(existing)).thenReturn(saved);
        when(modelMapper.map(eq(saved), eq(ShipmentDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/shipments/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentNumber").value(111))
                .andExpect(jsonPath("$.currLocation").value("Door 10"));
    }

    @Test
    void updateShipment_assignsTrailer_returnsDto() throws Exception {
        ShipmentUpdateRequestDTO request = new ShipmentUpdateRequestDTO();
        request.setTrailerNumber("T-99");

        Shipment existing = new Shipment();
        existing.setShipmentNumber(111L);

        Trailer trailer = new Trailer();
        trailer.setTrailerNumber("T-99");

        Shipment saved = new Shipment();
        saved.setShipmentNumber(111L);
        saved.setTrailer(trailer);

        ShipmentDTO dto = new ShipmentDTO();
        dto.setShipmentNumber(111L);

        when(shipmentService.getShipmentByNumber(111L)).thenReturn(existing);
        when(trailerService.getTrailerByNumber("T-99")).thenReturn(trailer);
        when(shipmentService.saveShipment(existing)).thenReturn(saved);
        when(modelMapper.map(eq(saved), eq(ShipmentDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/shipments/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentNumber").value(111));
    }

    @Test
    void updateShipment_notFound_returns404() throws Exception {
        ShipmentUpdateRequestDTO request = new ShipmentUpdateRequestDTO();
        request.setCurrLocation("Door 10");

        when(shipmentService.getShipmentByNumber(999L)).thenReturn(null);

        mockMvc.perform(put("/api/shipments/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // 5. DELETE shipment
    @Test
    void deleteShipment_exists_returnsNoContent() throws Exception {
        when(shipmentService.deleteShipment(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/shipments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShipment_notFound_returns404() throws Exception {
        when(shipmentService.deleteShipment(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/shipments/99"))
                .andExpect(status().isNotFound());
    }
}
