package org.example.freighthandler.door;

import org.example.freighthandler.trailer.Trailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class DoorService {
    @Autowired
    private DoorRepository doorRepository;


    @PersistenceContext
    protected EntityManager entityManager;

    public Door getDoorInfo(int doorNumber) {
        return doorRepository.findByDoorNumber(doorNumber).orElse(null);
    }

    @Transactional
    public Door saveDoor(Door door) {
        door = doorRepository.saveAndFlush(door);
        entityManager.refresh(door);
        return door;
    }

    public List<Door> getAllDoors() {
        return doorRepository.findAll(); // or your JPA repository call
    }
}
