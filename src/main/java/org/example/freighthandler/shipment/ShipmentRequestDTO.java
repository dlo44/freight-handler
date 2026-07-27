package org.example.freighthandler.shipment;

public class ShipmentRequestDTO {
    private Long shipmentNumber;
    private String destination;
    private String shipperAddress;
    private String receiverAddress;
    private String currLocation;
    private String trailerNumber;
    private Long authorId;

    public ShipmentRequestDTO() {}

    public void setShipmentNumber(Long shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public Long getShipmentNumber() {
        return shipmentNumber;
    }

    public String getDestination() {
        return destination;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getCurrLocation() {
        return currLocation;
    }
    public String getTrailerNumber() {
        return trailerNumber;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public  void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


}
