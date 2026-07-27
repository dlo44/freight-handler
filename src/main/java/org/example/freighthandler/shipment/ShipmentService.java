package org.example.freighthandler.shipment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public Shipment getShipmentByNumber(Long shipmentNumber){
        return shipmentRepository.findByShipmentNumber(shipmentNumber)
                .orElse(null);
    }

    @Transactional
    public Shipment saveShipment(Shipment shipment){
        // 1. Save and flush to database
        Shipment saved = shipmentRepository.saveAndFlush(shipment);

        // 2. Clear the cache and find again to ensure all relationships
        // (like Trailer and OsdEntries) are fully populated for the DTO mapper
        entityManager.flush();
        entityManager.clear();

        return shipmentRepository.findById(saved.getId()).orElse(saved);
    }

    public boolean deleteShipment(Long shipmentId){
        try {
            if(shipmentRepository.existsById(shipmentId)){
                shipmentRepository.deleteById(shipmentId);
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Shipment> getShipmentsByTrailer(String trailerNumber) {
        return shipmentRepository.findByTrailer_TrailerNumber(trailerNumber);
    }
}
