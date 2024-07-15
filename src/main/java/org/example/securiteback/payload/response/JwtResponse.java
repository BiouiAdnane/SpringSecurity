package org.example.securiteback.payload.response;

import lombok.Getter;

import java.util.List;

@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private List<String> permissions;

    public JwtResponse(String accessToken, Long id, String email, String fullName, List<String> permissions) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.permissions = permissions;

    }

}