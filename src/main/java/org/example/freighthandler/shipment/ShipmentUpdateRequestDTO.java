package org.example.freighthandler.shipment;

public class ShipmentUpdateRequestDTO {
    private String currLocation;
    private String trailerNumber;
    private Long authorId;
    private String eventType;

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currentLocation) {
        this.currLocation = currentLocation;
    }
    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public  void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }



}
