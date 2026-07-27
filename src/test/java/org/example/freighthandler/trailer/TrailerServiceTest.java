package org.example.freighthandler.trailer;

import org.example.freighthandler.door.Door;
import org.example.freighthandler.door.DoorRepository;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TrailerService.class)

public class TrailerServiceTest {
    @MockBean
    private TrailerRepository trailerRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private TrailerService trailerService;

    @BeforeEach
    public void setup() {
        trailerService.entityManager = entityManager;
    }


    @Test
    public void testGetTrailerByDoorNumber() throws Exception {
        // Arrange
        Trailer testTrailer = new Trailer();
        testTrailer.setId(1L);
        testTrailer.setTrailerNumber("P420");


        // Mock the repository call used by findByDoor_DoorNumber
        Mockito.when(trailerRepository.findByDoor_DoorNumber(15))
                .thenReturn(Optional.of(testTrailer));

        // Act
        Trailer result = trailerService.findByDoorNumber(15);

        // Assert
        assertNotNull(result);
        assertEquals("P420", result.getTrailerNumber());
        assertEquals(1L, result.getId());
    }

}
