package com.example.wallet.dto;


import com.example.wallet.entity.Wallet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class WalletRequest {

    private Wallet walletId;
}
