package com.example.wallet.service;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.WalletRequest;

public interface PaymentService {

    public String checkPayment(PaymentCheckRequest paymentCheckRequest);

    public String addStatus(PaymentRequest paymentRequest, int id);

    public String setStatus(int id, WalletRequest walletRequest);

    public String rollbackPayment(int id, WalletRequest walletRequest);

    public String getByStatus(String status);
}
