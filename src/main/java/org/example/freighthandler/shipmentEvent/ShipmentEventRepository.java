package org.example.freighthandler.shipmentEvent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {
   List<ShipmentEvent> findByShipment_ShipmentNumber (Long shipmentNumber);
   List<ShipmentEvent> findByPerformedBy_UserId (Long userID);

}
