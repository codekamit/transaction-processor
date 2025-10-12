package com.orm.learn_orm.poc;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="author", schema="orm")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
    @SequenceGenerator(name = "author_seq", sequenceName = "author_sequence", allocationSize = 1000)
    private Long id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="email", nullable = false, unique = true)
    private String email;
    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Book> books;
}
