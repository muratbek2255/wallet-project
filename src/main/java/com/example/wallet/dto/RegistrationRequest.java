package com.example.wallet.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private Integer id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String phoneNumber;

    private String password;

}
