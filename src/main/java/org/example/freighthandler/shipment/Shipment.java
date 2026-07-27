package org.example.freighthandler.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.freighthandler.destination.Destination;
import org.example.freighthandler.osd.OsdEntry;
import org.example.freighthandler.trailer.Trailer;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
    Shipment Class simplified for project,
    some missing fields include number of handling units,
    weight, and dimensions.
 */
@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long shipmentNumber;

    @Enumerated(EnumType.STRING)
    private Destination destination;

    private String shipperAddress;
    private String receiverAddress;
    private String currLocation;

    // Relationship to trailer.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trailer_id")
    @JsonProperty("trailer")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Trailer trailer;

    // Relationship to OS&D entries
    @OneToMany(mappedBy = "shipment")
    private List<OsdEntry> osdEntries= new ArrayList<>();



    public Shipment() {}

    public Shipment(Long shipmentNumber, String shipperAddress,String receiverAddress,
                    String currLocation, Destination destination) {
        this.shipmentNumber = shipmentNumber;
        this.shipperAddress = shipperAddress;
        this.receiverAddress = receiverAddress;
        this.currLocation = currLocation;
        this.destination = destination;
    }
    public Long getId() {return id;}

    public String getCurrLocation() {
        return currLocation;
    }

    public Long getShipmentNumber() {
        return shipmentNumber;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getReceiverAddress() {return receiverAddress;}

    public Trailer getTrailer() {
        return trailer;
    }

    public Destination getDestination() {
        return destination;
    }

    public List<OsdEntry> getOsdEntries() {
        return osdEntries;
    }

    public void setOsdEntries(List<OsdEntry> osdEntries) {
        this.osdEntries = osdEntries;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public void setShipmentNumber(Long shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
