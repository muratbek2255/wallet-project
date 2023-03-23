package com.example.wallet.controller;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.WalletRequest;
import com.example.wallet.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/check")
    public ResponseEntity<String> checkPayment(@RequestBody PaymentCheckRequest paymentRequest) {
        return ResponseEntity.status(201).body(paymentService.checkPayment(paymentRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> addPayment(@RequestBody PaymentRequest paymentRequest,
                                             @PathVariable int id) {
        return ResponseEntity.status(201).body(paymentService.addStatus(paymentRequest, id));
    }

    @PutMapping("/setStatus/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody WalletRequest walletRequest) {
        return ResponseEntity.status(201).body(paymentService.setStatus(id, walletRequest));
    }

    @PutMapping("/rollbackStatus/{id}")
    public ResponseEntity<String> rollbackStatus(@PathVariable int id, WalletRequest walletRequest) {
        return ResponseEntity.status(201).body(paymentService.rollbackPayment(id, walletRequest));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getByStatus(@Param("status") String status) {
        return ResponseEntity.status(200).body(paymentService.getByStatus(status));
    }
}
