package org.example.freighthandler.osd;

import org.example.freighthandler.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OsdRepository extends JpaRepository<OsdEntry, Long> {
    List<OsdEntry> findByShipmentShipmentNumber(Long shipmentNumber);
    List<OsdEntry> findByAuthorId(Long authorId);
}
