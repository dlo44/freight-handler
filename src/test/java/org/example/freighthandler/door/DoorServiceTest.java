package org.example.freighthandler.door;

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
@WebMvcTest(value = DoorService.class)
public class DoorServiceTest {

    @MockBean
    private DoorRepository doorRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private DoorService doorService;

    @BeforeEach
    public void setup() {
        doorService.entityManager = entityManager;
    }

    @Test
    public void testGetDoorInfo_returnsDoor() {
        // Arrange
        Door door = new Door();
        door.setDoorNumber(15);
        door.setStatus(DoorStatus.EMPTY);

        Mockito.when(doorRepository.findByDoorNumber(15))
                .thenReturn(Optional.of(door));

        // Act
        Door result = doorService.getDoorInfo(15);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getDoorNumber());
        assertEquals(DoorStatus.EMPTY, result.getStatus());
    }

    @Test
    public void testGetDoorInfo_notFound_returnsNull() {
        // Arrange
        Mockito.when(doorRepository.findByDoorNumber(99))
                .thenReturn(Optional.empty());

        // Act
        Door result = doorService.getDoorInfo(99);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAllDoors_returnsList() {
        // Arrange
        Door door1 = new Door();
        door1.setDoorNumber(1);

        Door door2 = new Door();
        door2.setDoorNumber(2);

        Mockito.when(doorRepository.findAll())
                .thenReturn(Arrays.asList(door1, door2));

        // Act
        List<Door> result = doorService.getAllDoors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testSaveDoor_returnsSavedDoor() {
        // Arrange
        Door door = new Door();
        door.setDoorNumber(5);
        door.setStatus(DoorStatus.EMPTY);

        Mockito.when(doorRepository.saveAndFlush(door)).thenReturn(door);
        Mockito.doNothing().when(entityManager).refresh(door);

        // Act
        Door result = doorService.saveDoor(door);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getDoorNumber());
    }
}
