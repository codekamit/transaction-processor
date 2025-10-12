package com.orm.learn_orm.poc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    @NotBlank(message = "Author name cannot be blank")
    private String name;
    private String email;
    @NotEmpty
    private List<@NotNull BookDTO> books;
}
