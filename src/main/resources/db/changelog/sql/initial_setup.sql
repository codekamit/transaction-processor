CREATE SEQUENCE author_sequence
    START WITH 1
    INCREMENT BY 1000
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE book_sequence
    START WITH 1
    INCREMENT BY 1000
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;


CREATE TABLE author (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE book (
    id BIGINT NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    author_id BIGINT REFERENCES author(id)
);

-- Create indexes for better performance
CREATE INDEX idx_book_author_id ON book(author_id);
CREATE INDEX idx_author_email ON author(email);