package com.example.wallet.service;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.RollbackWalletRequest;

public interface PaymentService {

    public String checkPayment(PaymentCheckRequest paymentCheckRequest);

    public String createPayment(PaymentRequest paymentRequest, int id);

    public String confirmPayment(int id, RollbackWalletRequest walletRequest);

    public String rollbackPayment(int id, RollbackWalletRequest walletRequest);

    public String getByStatusPayment(String status);
}
