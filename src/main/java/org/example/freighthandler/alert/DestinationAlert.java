package org.example.freighthandler.alert;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "destination_alerts")
public class DestinationAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private long avgDwellMinutes;
    private boolean active;
    private Instant createdAt;

    public Long getId() { return id; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public long getAvgDwellMinutes() { return avgDwellMinutes; }
    public void setAvgDwellMinutes(long avgDwellMinutes) { this.avgDwellMinutes = avgDwellMinutes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
