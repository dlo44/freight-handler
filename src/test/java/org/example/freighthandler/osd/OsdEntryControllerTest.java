package org.example.freighthandler.osd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OsdEntryController.class)
public class OsdEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OsdService osdService;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper to build a full OsdEntry with all required relationships
    private OsdEntry buildOsdEntry(Long shipmentNumber, Long authorId) {
        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(shipmentNumber);

        User author = new User();
        author.setUserId(authorId);

        OsdEntry entry = new OsdEntry(
                2, OsdType.DAMAGED, OsdDamageType.CRUSHED,
                "Box crushed on arrival", author, shipment
        );
        entry.setCreatedAt(LocalDateTime.now());
        return entry;
    }

    @Test
    void createOsdEntry_returnsCreatedDto() throws Exception {
        OsdEntryDTO request = new OsdEntryDTO();
        request.setShipmentNumber(111L);
        request.setAuthorId(1L);
        request.setType("DAMAGED");
        request.setDamageType("CRUSHED");
        request.setQty(2);
        request.setDetails("Box crushed on arrival");

        OsdEntry savedEntry = buildOsdEntry(111L, 1L);

        when(osdService.saveOsdEntry(any(OsdEntryDTO.class))).thenReturn(savedEntry);

        mockMvc.perform(post("/api/osd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.qty").value(2))
                .andExpect(jsonPath("$.type").value("DAMAGED"))
                .andExpect(jsonPath("$.damageType").value("CRUSHED"))
                .andExpect(jsonPath("$.shipmentNumber").value(111))
                .andExpect(jsonPath("$.authorId").value(1));

        verify(osdService).saveOsdEntry(any(OsdEntryDTO.class));
    }

    @Test
    void createOsdEntry_serviceThrows_returnsBadRequest() throws Exception {
        OsdEntryDTO request = new OsdEntryDTO();
        request.setShipmentNumber(999L);
        request.setAuthorId(1L);
        request.setType("DAMAGED");

        when(osdService.saveOsdEntry(any(OsdEntryDTO.class)))
                .thenThrow(new RuntimeException("Shipment not found"));

        mockMvc.perform(post("/api/osd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOsdEntries_returnsListOfDtos() throws Exception {
        OsdEntry entry1 = buildOsdEntry(111L, 1L);
        OsdEntry entry2 = buildOsdEntry(111L, 1L);
        entry2.setType(OsdType.SHORT);

        when(osdService.getOsdEntriesByShipmentNumber(111L))
                .thenReturn(Arrays.asList(entry1, entry2));

        mockMvc.perform(get("/api/osd/111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shipmentNumber").value(111))
                .andExpect(jsonPath("$[0].authorId").value(1))
                .andExpect(jsonPath("$[1].type").value("SHORT"));
    }

    @Test
    void getOsdEntries_noEntries_returnsNotFound() throws Exception {
        when(osdService.getOsdEntriesByShipmentNumber(999L))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/osd/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
