package org.example.freighthandler.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DestinationAlertRepository extends JpaRepository<DestinationAlert, Long> {
    List<DestinationAlert> findByActiveTrue();
    Optional<DestinationAlert> findByDestinationAndActiveTrue(String destination);
}
