package com.example.wallet.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorDto {
    
    private String message;

    public ErrorDto(String localizedMessage) {
    }
}
