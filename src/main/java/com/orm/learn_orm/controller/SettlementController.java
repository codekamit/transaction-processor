package com.orm.learn_orm.controller;


import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.settlement_processor.SettlementProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/settlement")
@AllArgsConstructor
public class SettlementController {

    private final SettlementProcessor settlementProcessor;

    @PostMapping("upload")
    public ResponseEntity<String> uploadSettlement(@RequestBody MultipartFile file,
                                                   @RequestParam(value = "settlementType", required = true) SettlementType settlementType) throws IOException {
        settlementProcessor.processSettlement(file, settlementType);
        return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
    }
}
