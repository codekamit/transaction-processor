package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.AuthorDTO;
import com.orm.learn_orm.dto.BookDTO;
import com.orm.learn_orm.model.Author;
import com.orm.learn_orm.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IAuthorMapper {

    IAuthorMapper INSTANCE = Mappers.getMapper(IAuthorMapper.class);

    AuthorDTO getAuthorDTO(Author author);

    BookDTO getBookDTO(Book book);

    Author getAuthor(AuthorDTO authorDTO);

    Book getBook(BookDTO bookDTO);
}
