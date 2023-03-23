package com.example.wallet.dto;

import com.example.wallet.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {

    private BigDecimal price;

    private String accountCheck;

    private FavourRequest favourRequest;

    private Wallet walletId;
}
