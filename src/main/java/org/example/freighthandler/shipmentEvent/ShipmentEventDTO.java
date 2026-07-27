package org.example.freighthandler.shipmentEvent;

public class ShipmentEventDTO {
    private Long shipmentNumber;
    private String performedBy;
    private String shipmentEventType;
    private String location;
    private String trailerNumber;
    private String eventTime;

    public ShipmentEventDTO() {}

    public ShipmentEventDTO(Long shipmentNumber, String performedBy, String shipmentEventType, String location,
                            String trailerNumber, String eventTime) {
        this.shipmentNumber = shipmentNumber;
        this.performedBy = performedBy;
        this.shipmentEventType = shipmentEventType;
        this.location = location;
        this.trailerNumber = trailerNumber;
        this.eventTime = eventTime;
    }

    public Long getShipmentNumber() {
        return shipmentNumber;
    }
    public void setShipmentNumber(Long shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public  String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getShipmentEventType() {
        return shipmentEventType;
    }

    public void setShipmentEventType(String shipmentEventType) {
        this.shipmentEventType = shipmentEventType;
    }

    public String getLocation() {
        return location;
    }

    public  void setLocation(String location) {
        this.location = location;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public  String getEventTime() {
        return eventTime;
    }
}
