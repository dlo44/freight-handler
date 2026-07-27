package org.example.freighthandler.trailer;

public class TrailerDTO {
    private Long id;
    private String trailerNumber;
    private String status;      // ModelMapper converts DoorStatus Enum to String
    private String destination; // ModelMapper converts Destination Enum to String
    private Boolean loading;

    // Getters and Setters (Required for ModelMapper)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrailerNumber() { return trailerNumber; }
    public void setTrailerNumber(String trailerNumber) { this.trailerNumber = trailerNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Boolean getLoading() { return loading; }
    public void setLoading(Boolean loading) { this.loading = loading; }
}
