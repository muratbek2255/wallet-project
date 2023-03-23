package com.example.wallet.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class FavourRequest {

    private Integer id;

    private String title;

    private String description;
}
