package com.orm.learn_orm.payload_generator;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PayloadGenerator {

    // --- Data Models (POJOs) ---
    // @Data generates getters, setters, toString, etc.
    // @AllArgsConstructor generates a constructor with all fields.

    public static void main(String[] args) {
        System.out.println("Starting payload generation...");

        Faker faker = new Faker();
        Random random = ThreadLocalRandom.current();

        // Data for creating random books
        List<String> genres = List.of("Science Fiction", "Fantasy", "Mystery", "Thriller", "Non-Fiction", "History", "Biography", "Self-Help", "Technology", "Business", "Art");
        List<String> titleAdjectives = List.of("The Silent", "The Lost", "Digital", "Quantum", "Ancient", "Forgotten", "Hidden", "Cosmic", "Final", "Golden");
        List<String> titleNouns = List.of("River", "Fortress", "Journey", "Echo", "Paradox", "Legacy", "Code", "Empire", "Secret", "Garden");

        List<Author> allAuthors = new ArrayList<>();

        // Generate 10,000 unique author records
        for (int i = 0; i < 10000; i++) {
            int bookCount = random.nextInt(5) + 1;
            List<Book> books = new ArrayList<>();
            for (int j = 0; j < bookCount; j++) {
                String title = titleAdjectives.get(random.nextInt(titleAdjectives.size())) + " " + titleNouns.get(random.nextInt(titleNouns.size()));
                String genre = genres.get(random.nextInt(genres.size()));
                books.add(new Book(title, genre));
            }

            String name = faker.name().fullName();
            String email = name.toLowerCase().replace(" ", ".") + i + "@fakermail.com"; // Ensure email uniqueness
            allAuthors.add(new Author(name, email, books));
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter(); // For nicely formatted JSON
            writer.writeValue(new File("src/main/resources/test_json.json"), allAuthors);
            System.out.println("Successfully generated payload.json with 10,000 records.");
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
    static class Book {
        private String title;
        private String genre;
    }

    // --- Main Generation Logic ---

    @Data
    @AllArgsConstructor
    static class Author {
        private String name;
        private String email;
        private List<Book> books;
    }
}
