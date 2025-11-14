package com.orm.learn_orm.my_tests.transaction_test;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TestTransactionController {

    private final TransactionTest transactionTest;

    @GetMapping("test-transaction")
    public ResponseEntity<String> testTransaction() throws MyCheckedException {
        transactionTest.testTransactionBehavior();
        return new ResponseEntity<>("Method run completed", HttpStatus.OK);
    }
}
