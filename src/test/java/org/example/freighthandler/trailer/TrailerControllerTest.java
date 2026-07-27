package org.example.freighthandler.trailer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freighthandler.door.Door;
import org.example.freighthandler.door.DoorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TrailerController.class)
public class TrailerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrailerService trailerService;

    @MockBean
    private DoorService doorService;


    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private ModelMapper modelMapper;


    @BeforeEach
    public void setup() {
    }




    @Test
    void getTrailerByDoorNumber_returnsTrailerDto() throws Exception {
        Trailer trailer = new Trailer();
        trailer.setId(2L);
        trailer.setTrailerNumber("P77777");

        TrailerDTO dto = new TrailerDTO();
        dto.setId(2L);
        dto.setTrailerNumber("P77777");

        when(trailerService.findByDoorNumber(15)).thenReturn(trailer);
        when(modelMapper.map(eq(trailer), eq(TrailerDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/api/trailers/door/15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trailerNumber").value("P77777"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void getTrailerByNumber_returnsTrailerDto() throws Exception {
        Trailer trailer = new Trailer();
        trailer.setId(1L);
        trailer.setTrailerNumber("P411");

        TrailerDTO dto = new TrailerDTO();
        dto.setId(1L);
        dto.setTrailerNumber("P411");

        when(trailerService.getTrailerByNumber("P411")).thenReturn(trailer);
        when(modelMapper.map(eq(trailer), eq(TrailerDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/api/trailers/P411")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trailerNumber").value("P411"))
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    void createTrailer_returnsCreatedDto() throws Exception {
        Trailer input = new Trailer();
        input.setTrailerNumber("P411");

        Trailer saved = new Trailer();
        saved.setId(3L);
        saved.setTrailerNumber("P411");

        TrailerDTO dto = new TrailerDTO();
        dto.setId(3L);
        dto.setTrailerNumber("P411");

        when(trailerService.saveTrailer(Mockito.any(Trailer.class))).thenReturn(saved);
        when(modelMapper.map(eq(saved), eq(TrailerDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/trailers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trailerNumber").value("P411"))
                .andExpect(jsonPath("$.id").value(3));

        verify(trailerService).saveTrailer(Mockito.any(Trailer.class));
    }

}
