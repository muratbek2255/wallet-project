package com.example.wallet.service;

import com.example.wallet.dto.WalletRequest;
import com.example.wallet.entity.Wallet;

public interface WalletService {

    public Wallet getByIdWallet(int id);

    public String addWallet(WalletRequest walletRequest);
}
