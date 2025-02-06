package com.OnlineMarketplace.OnlineMarketplace.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String username;
}