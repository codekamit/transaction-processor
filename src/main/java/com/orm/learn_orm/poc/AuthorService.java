package com.orm.learn_orm.poc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {

    private static final IAuthorMapper AUTHOR_MAPPER = IAuthorMapper.INSTANCE;
    private final IAuthorRepo authorRepo;

    @Transactional(transactionManager = "ormTransactionManager", readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepo.findById(id).orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return AUTHOR_MAPPER.getAuthorDTO(author);
    }

    @Transactional(transactionManager = "ormTransactionManager", readOnly = true)
    public List<AuthorDTO> getAuthors() {
        List<Author> authors = authorRepo.findAll();
        return authors.stream()
                .map(AUTHOR_MAPPER::getAuthorDTO)
                .toList();
    }

    @Transactional(transactionManager = "ormTransactionManager", readOnly = true)
    public List<AuthorDTO> getAuthorsWithoutNPlus1() {
        List<Author> authors = authorRepo.findAllAuthorsWithBooksJoined();
        return authors.stream()
                .map(AUTHOR_MAPPER::getAuthorDTO)
                .toList();
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void addAuthor(AuthorDTO dto) {
        Author author = AUTHOR_MAPPER.getAuthor(dto);
        List<Book> books = dto.getBooks().stream()
                .map(AUTHOR_MAPPER::getBook)
                .peek(book -> book.setAuthor(author))
                .toList();
        author.setBooks(books);
        authorRepo.save(author);
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void addAuthors(List<AuthorDTO> dtos) {
        List<Author> authors = dtos.stream()
                .map(dto -> {
                    Author author = AUTHOR_MAPPER.getAuthor(dto);
                    List<Book> books = dto.getBooks().stream()
                            .map(AUTHOR_MAPPER::getBook)
                            .peek(book -> book.setAuthor(author))
                            .toList();
                    author.setBooks(books);
                    return author;
                })
                .toList();
        authorRepo.saveAll(authors);
    }
}
