package com.example.wallet.service;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.RollbackWalletRequest;
import com.example.wallet.entity.Favour;
import com.example.wallet.entity.Payment;
import com.example.wallet.entity.PaymentStatus;
import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, FavourServiceImpl favourService,
                              WalletServiceImpl walletService, KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.favourService = favourService;
        this.walletService = walletService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public String checkPayment(PaymentCheckRequest paymentCheckRequest) {

        if(paymentCheckRequest.getSumOfFavour() > 500 && paymentCheckRequest.getSumOfFavour() < 25000 &&
                paymentCheckRequest.getAccountCheck() != null) {

            Payment payment = new Payment();

            payment.setIsChecked(Boolean.TRUE);
            payment.setStatus(PaymentStatus.STATUS_PROCESS);

            paymentRepository.save(payment);

            kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                    " and his payment is checked: " + payment.getIsChecked());

            return "Order is checked!";

        } else  {

            return "Error, you have problem with sumOfFavour or AccountCheck!";

        }

    }

    @Transactional
    @Override
    public String createPayment(PaymentRequest paymentRequest, int id) {


        Payment payment = paymentRepository.getById(id);

        Favour favour = favourService.getByIdFavor(paymentRequest.getFavourRequest().getId());

        Wallet wallet = walletService.getByIdWallet(paymentRequest.getWalletId().getId());

        if(wallet.getSumma().compareTo(paymentRequest.getPrice()) < 0) {

            return "You don't have money! Sorry!";
        }

        if(payment.getIsChecked().equals(Boolean.TRUE)) {

            payment.setStatus(PaymentStatus.STATUS_CREATED);
            payment.setCreated_at(Timestamp.from(Instant.now()));
            payment.setFinished(Boolean.FALSE);
            payment.setFavour(favour);
            payment.setSumOfFavour(paymentRequest.getPrice());
            payment.setAccountCheck(paymentRequest.getAccountCheck());
            wallet.setSumma(wallet.getSumma().subtract(payment.getSumOfFavour()));

            kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                    " and his payment status: " + payment.getStatus());

            paymentRepository.save(payment);
            return "Payment Created";
        }else {
            return "You have some with problem with AccountCheck or SumOfFavour";
        }

    }

    @Transactional
    @Override
    public String confirmPayment(int id, RollbackWalletRequest walletRequest) {

        Payment payment = paymentRepository.getById(id);

        payment.setFinished(Boolean.TRUE);

        if(payment.getFinished().equals(Boolean.TRUE)) {
            payment.setStatus(PaymentStatus.STATUS_SUCCESS);
        }
        payment.setUpdated_at(Timestamp.from(Instant.now()));

        paymentRepository.save(payment);

        kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                " and his payment status confirm: " + payment.getStatus());

        return "Your status in payment: " + payment.getStatus();
    }

    @Override
    public String rollbackPayment(int id, RollbackWalletRequest walletRequest) {
        Payment payment = paymentRepository.getById(id);

        Wallet wallet = walletService.getByIdWallet(walletRequest.getWalletId().getId());

        if(payment.getStatus().equals(PaymentStatus.STATUS_SUCCESS)) {

            long Milli = Math.abs(payment.getUpdated_at().getTime() - new Date().getTime());

            if(Milli < 1080000) {
                payment.setStatus(PaymentStatus.STATUS_ROLLBACK);
                wallet.setSumma(wallet.getSumma().add(payment.getSumOfFavour()));
                kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                        " and his payment status confirm: " + payment.getStatus());
            } else {
                kafkaTemplate.send("payment", "Payment id: " + payment.getId() + " not to rollbacked");
                return "3 days gone";
            }
        } else if(payment.getStatus().equals(PaymentStatus.STATUS_CREATED)) {
            payment.setStatus(PaymentStatus.STATUS_ROLLBACK);
            wallet.setSumma(wallet.getSumma().add(payment.getSumOfFavour()));
            kafkaTemplate.send("payment", "Payment id: " + payment.getId() +
                    " and his payment status confirm: " + payment.getStatus());
        } else {
            return "You dont have payment or your status fail";
        }

        payment.setUpdated_at(Timestamp.from(Instant.now()));

        paymentRepository.save(payment);

        return "Rollback payment";
    }

    @Override
    public String getByStatusPayment(String status) {

        List<Payment> payment = paymentRepository.getByStatus(status);

        return "Payment with status: " + payment;

    }

    @Scheduled(initialDelay = 1000L, fixedDelay = 3000L)
    public void getAllStatusPayment() {

        System.out.println("Hello world!");
    }
}
