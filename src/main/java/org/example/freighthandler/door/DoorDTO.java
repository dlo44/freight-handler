package org.example.freighthandler.door;


import org.example.freighthandler.trailer.TrailerDTO;

public class DoorDTO {
    private int doorNumber;
    private String status;      // ModelMapper converts DoorStatus Enum to String
    private TrailerDTO trailer; // Use DTO instead of Entity

    public DoorDTO() {}

    // Getters and Setters (Required for ModelMapper)


    public int getDoorNumber() { return doorNumber; }
    public void setDoorNumber(int doorNumber) { this.doorNumber = doorNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public TrailerDTO getTrailer() { return trailer; }
    public void setTrailer(TrailerDTO trailer) { this.trailer = trailer; }
}