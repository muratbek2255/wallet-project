package com.example.wallet.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class WalletRequest {

    private Integer id;

    private BigDecimal summaOfWallet;

    private UserRequest userRequest;
}
