package org.example.freighthandler.door;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.freighthandler.trailer.Trailer;

import jakarta.persistence.*;

@Entity
@Table(name = "doors")
public class Door {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private int doorNumber;

    @Enumerated(EnumType.STRING)
    private DoorStatus status;

    @OneToOne
    @JoinColumn(name = "trailer_id")
    @JsonProperty("trailer")
    @JsonManagedReference
    private Trailer trailer;

    public Door() {}

    public Door(int doorNumber,  DoorStatus status) {
        this.doorNumber = doorNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public int getDoorNumber() {
        return doorNumber;
    }
    public void setDoorNumber(int doorNumber) {
        this.doorNumber = doorNumber;
    }

    public DoorStatus getStatus() {
        return status;
    }
    public void setStatus(DoorStatus status) {
        this.status = status;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer= trailer;
    }

}

