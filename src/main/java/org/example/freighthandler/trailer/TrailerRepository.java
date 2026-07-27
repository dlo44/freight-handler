package org.example.freighthandler.trailer;

import org.example.freighthandler.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrailerRepository  extends JpaRepository<Trailer, Long> {
   Optional<Trailer> findByTrailerNumber(String trailerNumber);
   Optional<Trailer> findByDoor_DoorNumber(int doorNumber);
}
