package org.example.freighthandler.osd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.user.User;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "osdEntries")
public class OsdEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int qty;

    @Enumerated(EnumType.STRING)
    private OsdType type;

    @Enumerated(EnumType.STRING)
    private OsdDamageType damageType;

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime createdAt;

    //Relationship to user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User author;

    //Relationship to Shipment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    @JsonIgnore
    private Shipment shipment;

    public OsdEntry() {}

    public OsdEntry(int qty, OsdType type, OsdDamageType damageType, String details, User author, Shipment shipment) {
        this.qty = qty;
        this.type = type;
        this.damageType = damageType;
        this.details = details;
        this.author = author;
        this.shipment = shipment;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Long getId() {
        return id;
    }
    public int getQty() {
        return qty;
    }

    public void setType(OsdType type) {
        this.type = type;
    }

    public OsdType getType() {
        return type;
    }

    public void setDamageType(OsdDamageType damageType) {
        this.damageType = damageType;
    }
    public OsdDamageType getDamageType() {
        return damageType;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public String getDetails() {
        return details;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }


    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Shipment getShipment() {
        return shipment;
    }


}
