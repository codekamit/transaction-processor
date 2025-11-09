package com.orm.learn_orm.controller;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.service.ClientPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RequestMapping("/api/client-preference")
@RestController
public class ClientPreferenceController {

    private final ClientPreferenceService service;

    /**
     * CREATE a new client preference.
     * POST /api/client-preference
     */
    @PostMapping("add")
    public ResponseEntity<String> createPreference(@RequestBody ClientPreferenceDTO dto) {
        service.createPreference(dto);
        return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
    }


    @PostMapping("add-all")
    public ResponseEntity<String> createPreferences(@RequestBody List<ClientPreferenceDTO> dtos) {
        service.createPreferences(dtos);
        return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("get-all-by-key")
    public ResponseEntity<List<ClientPreferenceDTO>> getPreferencesByKey(@RequestBody List<ClientPrefKey> keys) {
        return new ResponseEntity<>(service.getPreferencesByKeys(keys), HttpStatus.CREATED);
    }

    @PostMapping("edit")
    public ResponseEntity<String> updateClientPreference(@RequestBody ClientPreferenceDTO dto) {
        service.updateClientPreference(dto);
        return ResponseEntity.ok("Updated Successfully");
    }

    /**
     * READ a client preference by its business key (clientName and currency).
     * GET /api/client-preference?clientName=SomeClient&currency=USD
     */
    @GetMapping("get-by-business-key")
    public ResponseEntity<ClientPreferenceDTO> getPreferenceByBusinessKey(
            @RequestParam String clientName,
            @RequestParam Currency currency) {
        return ResponseEntity.ok(service.getPreferenceByBusinessKey(clientName, currency));
    }

    /**
     * READ all client preferences.
     * GET /api/client-preference/all
     */
    @GetMapping("get-all")
    public ResponseEntity<List<ClientPreferenceDTO>> getAllPreferences() {
        return ResponseEntity.ok(service.getAllPreferences());
    }

    /**
     * UPDATE an existing client preference by its surrogate ID.
     * PUT /api/client-preference/123
     */
    @PutMapping("update/{id}")
    public ResponseEntity<String> updatePreference(
            @PathVariable UUID id,
            @RequestBody ClientPreferenceDTO dto) {
        service.updatePreference(id, dto);
        return ResponseEntity.ok("Updated Successfully");
    }

    /**
     * DELETE a client preference by its surrogate ID.
     * DELETE /api/client-preference/123
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable UUID id) {
        service.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}