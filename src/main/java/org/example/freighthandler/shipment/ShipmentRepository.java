package org.example.freighthandler.shipment;

import org.example.freighthandler.trailer.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByShipmentNumber(Long shipmentNumber);

    Boolean existsByShipmentNumber(Long shipmentNumber);

    void deleteByShipmentNumber(Long shipmentNumber);
    List<Shipment> findByTrailer_TrailerNumber(String trailerNumber);


}
