package org.example.freighthandler.door;

public class DoorUpdateRequestDTO {
    private int doorNumber;
    private String Status;
    private String trailerNumber;

    public DoorUpdateRequestDTO() {}

    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        this.Status = status;
    }

    public  String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public  void setDoorNumber(int doorNumber) {
        this.doorNumber = doorNumber;
    }
}
