package com.example.locationTracker.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String phoneNumber;
    private String name;
    private String email;
    private String password;
    private double lat;
    private double lon;

    @JsonProperty("pKey")
    private String pKey;
}