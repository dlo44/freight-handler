package org.example.freighthandler.shipmentEvent;

import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class ShipmentEventService {
    @Autowired
    private ShipmentEventRepository shipmentEventRepository;

   public ShipmentEvent saveShipmentEvent(Shipment shipment, User performedBy,
                                          ShipmentEventType eventType,
                                          String trailerNumber,
                                          String location) {
       ShipmentEvent event = new ShipmentEvent(
               shipment,
               performedBy,
               eventType,
               trailerNumber,
               location
       );
       return shipmentEventRepository.save(event);
   }

    public List<ShipmentEvent> getShipmentEventByShipmentNumber(Long shipmentNumber){
        return shipmentEventRepository.findByShipment_ShipmentNumber(shipmentNumber);

    }

    public List<ShipmentEvent> getShipmentEventByUserID(Long userID){
        return shipmentEventRepository.findByPerformedBy_UserId(userID);
    }


}
