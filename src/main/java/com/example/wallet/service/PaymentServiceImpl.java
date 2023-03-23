package com.example.wallet.service;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.WalletRequest;
import com.example.wallet.entity.Favour;
import com.example.wallet.entity.Payment;
import com.example.wallet.entity.PaymentStatus;
import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private final FavourServiceImpl favourService;

    private final WalletServiceImpl walletService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, FavourServiceImpl favourService, WalletServiceImpl walletService) {
        this.paymentRepository = paymentRepository;
        this.favourService = favourService;
        this.walletService = walletService;
    }


    @Override
    public String checkPayment(PaymentCheckRequest paymentCheckRequest) {

        if(paymentCheckRequest.getSumOfFavour() > 500 && paymentCheckRequest.getSumOfFavour() < 25000 &&
                paymentCheckRequest.getAccountCheck() != null) {

            Payment payment = new Payment();

            payment.setIsChecked(Boolean.TRUE);
            payment.setStatus(PaymentStatus.STATUS_PROCESS);

            paymentRepository.save(payment);

            return "Order is checked!";

        } else  {

            return "Error, you have problem with sumOfFavour or AccountCheck!";

        }

    }

    @Override
    public String addStatus(PaymentRequest paymentRequest, int id) {


        Payment payment = paymentRepository.getById(id);

        Favour favour = favourService.getByIdFavor(paymentRequest.getFavourRequest().getId());

        if(payment.getIsChecked().equals(Boolean.TRUE)) {

            payment.setStatus(PaymentStatus.STATUS_CREATED);
            payment.setCreated_at(Timestamp.from(Instant.now()));
            payment.setFinished(Boolean.FALSE);
            payment.setFavour(favour);
            payment.setSumOfFavour(paymentRequest.getPrice());
            payment.setAccountCheck(paymentRequest.getAccountCheck());

            paymentRepository.save(payment);

            return "Payment Created";
        }else {
            return "You have some with problem with AccountCheck or SumOfFavour";
        }

    }

    @Override
    public String setStatus(int id, WalletRequest walletRequest) {

        Payment payment = paymentRepository.getById(id);

        Wallet wallet = walletService.getByIdWallet(walletRequest.getWalletId().getId());

        payment.setFinished(Boolean.TRUE);

        if(payment.getFinished().equals(Boolean.TRUE)) {
            payment.setStatus(PaymentStatus.STATUS_SUCCESS);
            wallet.setSumma(wallet.getSumma().subtract(payment.getSumOfFavour()));
        }
        payment.setUpdated_at(Timestamp.from(Instant.now()));

        paymentRepository.save(payment);

        return "Your status in payment: " + payment.getStatus();
    }

    @Override
    public String rollbackPayment(int id, WalletRequest walletRequest) {
        Payment payment = paymentRepository.getById(id);

        Wallet wallet = walletService.getByIdWallet(walletRequest.getWalletId().getId());

        if(payment.getStatus().equals(PaymentStatus.STATUS_SUCCESS)) {

            long Milli = Math.abs(payment.getUpdated_at().getTime() - new Date().getTime());

            if(Milli < 1080000) {
                payment.setStatus(PaymentStatus.STATUS_ROLLBACK);
                wallet.setSumma(wallet.getSumma().add(payment.getSumOfFavour()));
            } else {
                return "3 days gone";
            }
        } else if(payment.getStatus().equals(PaymentStatus.STATUS_CREATED)) {
            payment.setStatus(PaymentStatus.STATUS_ROLLBACK);
        } else {
            return "You dont have payment or your status fail";
        }

        payment.setUpdated_at(Timestamp.from(Instant.now()));

        paymentRepository.save(payment);

        return "Rollback payment";
    }

    @Override
    public String getByStatus(String status) {

        List<Payment> payment = paymentRepository.getByStatus(status);

        return "Payment with status: " + payment;

    }
}
