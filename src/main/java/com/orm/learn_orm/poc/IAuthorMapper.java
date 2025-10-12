package com.orm.learn_orm.poc;

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
