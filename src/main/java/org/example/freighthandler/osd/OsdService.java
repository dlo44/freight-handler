package org.example.freighthandler.osd;

import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.shipment.ShipmentRepository;
import org.example.freighthandler.user.User;
import org.example.freighthandler.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class OsdService {

    @Autowired
    private OsdRepository osdRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OsdEntry saveOsdEntry(OsdEntryDTO osdEntryDTO) {

        Shipment shipment = shipmentRepository
                .findByShipmentNumber(osdEntryDTO.getShipmentNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Shipment not found: " + osdEntryDTO.getShipmentNumber()));

        User author = userRepository
                .findByUserId(osdEntryDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + osdEntryDTO.getAuthorId()));

        OsdType type = OsdType.valueOf(osdEntryDTO.getType().toUpperCase());

        OsdDamageType damageType = osdEntryDTO.getDamageType() != null
                ? OsdDamageType.valueOf(osdEntryDTO.getDamageType().toUpperCase())
                : null;

        OsdEntry entry = new OsdEntry(
                osdEntryDTO.getQty(),
                type,
                damageType,
                osdEntryDTO.getDetails(),
                author,
                shipment
        );

        return osdRepository.save(entry);
    }

    public List<OsdEntry>getOsdEntriesByShipmentNumber(Long shipmentNumber) {
        return osdRepository.findByShipmentShipmentNumber(shipmentNumber);
    }

}