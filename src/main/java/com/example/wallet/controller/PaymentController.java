package com.example.wallet.controller;

import com.example.wallet.dto.PaymentCheckRequest;
import com.example.wallet.dto.PaymentRequest;
import com.example.wallet.dto.RollbackWalletRequest;
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
        return ResponseEntity.status(201).body(paymentService.createPayment(paymentRequest, id));
    }

    @PutMapping("/setStatus/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody RollbackWalletRequest walletRequest) {
        return ResponseEntity.status(201).body(paymentService.confirmPayment(id, walletRequest));
    }

    @PutMapping("/rollbackStatus/{id}")
    public ResponseEntity<String> rollbackStatus(@PathVariable int id, @RequestBody RollbackWalletRequest walletRequest) {
        return ResponseEntity.status(201).body(paymentService.rollbackPayment(id, walletRequest));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getByStatus(@Param("status") String status) {
        return ResponseEntity.status(200).body(paymentService.getByStatusPayment(status));
    }
}
