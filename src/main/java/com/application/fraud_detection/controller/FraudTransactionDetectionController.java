package com.application.fraud_detection.controller;


import com.application.fraud_detection.entity.Transaction;
import com.application.fraud_detection.service.FraudDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Slf4j
@Controller
public class FraudTransactionDetectionController {

    @Autowired
    Transaction transaction;
    @Autowired
    FraudDetectionService fraudDetectionService;

    @PostMapping("/isFraud")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) throws IOException {
        log.info("Request recieved in controller");
        boolean isFraud = fraudDetectionService.isFraud(transaction);
        transaction.setFraud(isFraud);
        return ResponseEntity.ok(transaction);
    }

}
