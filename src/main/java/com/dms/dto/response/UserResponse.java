package com.dms.dto.response;

import java.time.Instant;

import lombok.Data;

@Data
public class UserResponse {

    private String username;
    private String email;
    private String status;
    private Instant creationDate;
    
   public UserResponse(String username, String email, String status, Instant creationDate) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.creationDate = creationDate;
    }

    
}
