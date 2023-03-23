package com.example.wallet.dto;


import java.util.List;

public class JwtParseResponseDto {

    private String email;

    private List<String> authorities;

    public JwtParseResponseDto() {
    }

    public JwtParseResponseDto(String email, List<String> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}