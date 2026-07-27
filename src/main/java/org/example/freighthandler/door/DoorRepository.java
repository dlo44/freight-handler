package org.example.freighthandler.door;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoorRepository  extends JpaRepository<Door, Long> {
    Optional<Door> findByDoorNumber(int doorNumber);


}
