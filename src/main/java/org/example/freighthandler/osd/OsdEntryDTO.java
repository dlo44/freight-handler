package org.example.freighthandler.osd;

public class OsdEntryDTO {
    private Long id;
    private int qty;
    private String type;        // OVER, SHORT, DAMAGED
    private String damageType;  // CRUSHED, TORN, etc.
    private String details;
    // Flattens User.fullName
    private String createdAt;
    private Long shipmentNumber;
    private Long authorId;// ISO String

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDamageType() { return damageType; }
    public void setDamageType(String damageType) { this.damageType = damageType; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Long getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber (Long shipmentNumber) { this.shipmentNumber = shipmentNumber; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}
