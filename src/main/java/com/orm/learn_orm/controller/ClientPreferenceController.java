package com.orm.learn_orm.controller;

import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.service.ClientPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RequestMapping("/api/client-preference")
@RestController
public class ClientPreferenceController {

    private final ClientPreferenceService service;

    /**
     * CREATE a new client preference.
     * POST /api/client-preference
     */
    @PostMapping
    public ResponseEntity<ClientPreferenceDTO> createPreference(@RequestBody ClientPreferenceDTO dto) {
        ClientPreferenceDTO created = service.createPreference(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * READ a client preference by its business key (clientName and currency).
     * GET /api/client-preference?clientName=SomeClient&currency=USD
     */
    @GetMapping
    public ResponseEntity<ClientPreferenceDTO> getPreferenceByBusinessKey(
            @RequestParam String clientName,
            @RequestParam Currency currency) {
        return ResponseEntity.ok(service.getPreferenceByBusinessKey(clientName, currency));
    }

    /**
     * READ all client preferences.
     * GET /api/client-preference/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClientPreferenceDTO>> getAllPreferences() {
        return ResponseEntity.ok(service.getAllPreferences());
    }

    /**
     * UPDATE an existing client preference by its surrogate ID.
     * PUT /api/client-preference/123
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientPreferenceDTO> updatePreference(
            @PathVariable Long id,
            @RequestBody ClientPreferenceDTO dto) {
        return ResponseEntity.ok(service.updatePreference(id, dto));
    }

    /**
     * DELETE a client preference by its surrogate ID.
     * DELETE /api/client-preference/123
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        service.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}