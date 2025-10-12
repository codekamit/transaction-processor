package com.orm.learn_orm.controller;


import com.orm.learn_orm.dto.AuthorDTO;
import com.orm.learn_orm.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("get")
    public ResponseEntity<AuthorDTO> getAuthorById(@RequestParam(value="id", required=true) long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("get-all-with-n1")
    public ResponseEntity<List<AuthorDTO>> getAuthorsWithNPlus1() {
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @GetMapping("get-all-without-n1")
    public ResponseEntity<List<AuthorDTO>> getAuthorsWithoutNPlus1() {
        return ResponseEntity.ok(authorService.getAuthorsWithoutNPlus1());
    }

    @PostMapping("add")
    public ResponseEntity<String> addAuthor(@Valid @RequestBody AuthorDTO dto) {
        authorService.addAuthor(dto);
        return ResponseEntity.ok("Done");
    }

    @PostMapping("add-all")
    public ResponseEntity<String> addAuthors(@Valid @RequestBody @NotEmpty List<@Valid AuthorDTO> dtos) {
        authorService.addAuthors(dtos);
        return ResponseEntity.ok("Done");
    }
}
