package org.example.freighthandler.door;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DoorController.class)
public class DoorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoorService doorService;

    @MockBean
    private TrailerService trailerService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getDoorInfo_returnsDoorDto() throws Exception {
        Door door = new Door();
        door.setDoorNumber(15);
        door.setStatus(DoorStatus.EMPTY);

        DoorDTO dto = new DoorDTO();
        dto.setDoorNumber(15);
        dto.setStatus("EMPTY");

        when(doorService.getDoorInfo(15)).thenReturn(door);
        when(modelMapper.map(eq(door), eq(DoorDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/api/doors/15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doorNumber").value(15))
                .andExpect(jsonPath("$.status").value("EMPTY"));
    }

    @Test
    void getDoorInfo_notFound_returns404() throws Exception {
        when(doorService.getDoorInfo(99)).thenReturn(null);

        mockMvc.perform(get("/api/doors/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDoors_returnsListOfDtos() throws Exception {
        Door door1 = new Door();
        door1.setDoorNumber(1);

        Door door2 = new Door();
        door2.setDoorNumber(2);

        DoorDTO dto1 = new DoorDTO();
        dto1.setDoorNumber(1);

        DoorDTO dto2 = new DoorDTO();
        dto2.setDoorNumber(2);

        when(doorService.getAllDoors()).thenReturn(Arrays.asList(door1, door2));
        when(modelMapper.map(eq(door1), eq(DoorDTO.class))).thenReturn(dto1);
        when(modelMapper.map(eq(door2), eq(DoorDTO.class))).thenReturn(dto2);

        mockMvc.perform(get("/api/doors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].doorNumber").value(1))
                .andExpect(jsonPath("$[1].doorNumber").value(2));
    }

    @Test
    void updateDoorAssignment_assignsTrailer_returnsDto() throws Exception {
        // The update request body
        DoorUpdateRequestDTO request = new DoorUpdateRequestDTO();
        request.setStatus("LOAD");
        request.setTrailerNumber("T-99");

        Door existingDoor = new Door();
        existingDoor.setDoorNumber(15);
        existingDoor.setStatus(DoorStatus.EMPTY);

        Trailer trailer = new Trailer();
        trailer.setTrailerNumber("T-99");

        Door savedDoor = new Door();
        savedDoor.setDoorNumber(15);
        savedDoor.setStatus(DoorStatus.LOAD);

        DoorDTO dto = new DoorDTO();
        dto.setDoorNumber(15);
        dto.setStatus("LOAD");

        when(doorService.getDoorInfo(15)).thenReturn(existingDoor);
        when(trailerService.getTrailerByNumber("T-99")).thenReturn(trailer);
        when(doorService.saveDoor(existingDoor)).thenReturn(savedDoor);
        when(modelMapper.map(eq(savedDoor), eq(DoorDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/doors/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doorNumber").value(15))
                .andExpect(jsonPath("$.status").value("LOAD"));
    }

    @Test
    void updateDoorAssignment_doorNotFound_returns404() throws Exception {
        DoorUpdateRequestDTO request = new DoorUpdateRequestDTO();
        request.setStatus("LOAD");

        when(doorService.getDoorInfo(99)).thenReturn(null);

        mockMvc.perform(put("/api/doors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
