package com.example.wallet.service;

import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet getByIdWallet(int id) {

        Wallet wallet = walletRepository.getById(id);

        return wallet;
    }
}
