package org.example.freighthandler.shipmentEvent;

import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_events")
public class ShipmentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id", nullable = false)
    private User performedBy;

    @Enumerated (EnumType.STRING)
    private ShipmentEventType eventType;

    private String trailerNumber;
    private String location;
    private LocalDateTime eventTime;

    @PrePersist
    protected void onCreate() {
        this.eventTime = LocalDateTime.now();
    }

    public ShipmentEvent() {}

    public ShipmentEvent(Shipment shipment, User performedBy, ShipmentEventType eventType,
                         String trailerNumber, String location) {
        this.shipment = shipment;
        this.performedBy = performedBy;
        this.eventType = eventType;
        this.trailerNumber = trailerNumber;
        this.location = location;

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

   public User getPerformedBy() {
        return performedBy;
   }

   public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
   }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrailerNumber() {
        return  trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public ShipmentEventType getEventType() {
        return eventType;
    }

    public void setEventType(ShipmentEventType eventType) {
        this.eventType = eventType;
    }




}
