package org.example.freighthandler.trailer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.example.freighthandler.destination.Destination;
import org.example.freighthandler.door.Door;
import org.example.freighthandler.door.DoorStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "trailers")
public class Trailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trailerNumber;

    @Enumerated(EnumType.STRING)
    private DoorStatus status;

    @OneToOne(mappedBy = "trailer")
    @JsonBackReference
    private Door door;

    @Column(name = "is_loading")
    private Boolean loading ;

    @Enumerated(EnumType.STRING)
    private Destination destination;



    public Trailer() {}

    public Trailer(String trailerNumber, DoorStatus status,Destination destination) {
        this.trailerNumber = trailerNumber;
        this.status = status;
        this.destination = destination;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrailerNumber() { return trailerNumber; }
    public void setTrailerNumber(String trailerNumber) { this.trailerNumber = trailerNumber; }

    public DoorStatus getStatus() { return status; }
    public void setStatus(DoorStatus status) { this.status = status; }

    public Door getDoor() { return door; }
    public void setDoor(Door door) { this.door = door; }

    public Boolean getLoading() {
        return this.loading;
    }

    public void setLoading(Boolean loading) {
        this.loading = loading;
    }

    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }
}
