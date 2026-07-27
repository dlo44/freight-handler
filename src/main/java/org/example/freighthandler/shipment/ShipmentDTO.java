package org.example.freighthandler.shipment;

import org.example.freighthandler.osd.OsdEntryDTO;
import org.example.freighthandler.trailer.TrailerDTO;


import java.util.List;

public class ShipmentDTO {
    private Long id;
    private Long shipmentNumber;
    private String destination;     // ModelMapper will convert the Enum to a String
    private String shipperAddress;
    private String receiverAddress;
    private String currLocation;

    // This allows you to show the Trailer Number in the UI without
    // sending the entire Trailer object (prevents infinite recursion)
    private TrailerDTO trailer;

    // This will hold the history of Over, Short, or Damaged reports
    private List<OsdEntryDTO> osdEntries;

    // Default Constructor
    public ShipmentDTO() {}

    // Getters and Setters (Required for ModelMapper to work!)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber(Long shipmentNumber) { this.shipmentNumber = shipmentNumber; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getShipperAddress() { return shipperAddress; }
    public void setShipperAddress(String shipperAddress) { this.shipperAddress = shipperAddress; }

    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }

    public String getCurrLocation() { return currLocation; }
    public void setCurrLocation(String currLocation) { this.currLocation = currLocation; }

    public TrailerDTO getTrailer() { return trailer; }
    public void setTrailer(TrailerDTO trailer) { this.trailer = trailer; }

    public List<OsdEntryDTO> getOsdEntries() { return osdEntries; }
    public void setOsdEntries(List<OsdEntryDTO> osdEntries) { this.osdEntries = osdEntries; }
}
