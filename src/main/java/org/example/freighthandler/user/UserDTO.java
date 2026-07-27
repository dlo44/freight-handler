package org.example.freighthandler.user;

public class UserDTO {
    private Long id;
    private Long userId; // The Employee ID (Badge number)
    private String name;

    // Getters and Setters (Required for ModelMapper)
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
