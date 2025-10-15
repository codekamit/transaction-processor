package com.orm.learn_orm.controller;


import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.settlement_processor.SettlementProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/settlement")
@AllArgsConstructor
public class SettlementController {

    private final SettlementProcessor settlementProcessor;

    @PostMapping("upload")
    public ResponseEntity<String> uploadSettlement(@RequestBody MultipartFile file,
                                                   @RequestParam(value = "settlementType", required = true) SettlementType settlementType) throws IOException {
        settlementProcessor.processSettlement(file, settlementType);
        return new ResponseEntity<>("Uploaded Successfully", HttpStatus.CREATED);
    }

    @PostMapping("suspend-netted-settlement")
    public ResponseEntity<String> suspendNetSettlement(@RequestParam(value = "id", required = true) UUID id,
                                                       @RequestParam(value = "settlementType", required = true) SettlementType settlementType) {
        settlementProcessor.suspendNettedSettlement(id, settlementType);
        return new ResponseEntity<>("Suspended Successfully", HttpStatus.OK);
    }

    @PostMapping("reprocess-default-pref")
    public ResponseEntity<String> reprocessDefaultPreference() {
        settlementProcessor.reprocessDefaultPreference(SettlementType.EARNING);
        return new ResponseEntity<>("Reprocessed Successfully", HttpStatus.OK);
    }
}
