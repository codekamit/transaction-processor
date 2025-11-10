package com.orm.learn_orm.my_tests.workbook_test;

import com.orm.learn_orm.my_tests.workbook_test.generator.CsvGenerator;
import com.orm.learn_orm.my_tests.workbook_test.generator.WorkBookGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class ExcelService {

    private final WorkBookGenerator workBookGenerator;
    private final CsvGenerator csvGenerator;
    private final WebClient localApiClient;

    public List<AuthorsDTO> dummyData() {
        return Arrays.asList(
                new AuthorsDTO(
                        "Stephen King",
                        43,
                        "Sambhalpur",
                        Arrays.asList(new BooksDTO("The rude king", "GreyScale Publication", LocalDate.now(), "Drama"),
                                new BooksDTO("The night murder", "Harper Collins", LocalDate.of(2022, 12, 3), "Crime Drama")),
                        "The author was nominated twice for booker prize",
                        10,20,30
                ),
                new AuthorsDTO(
                        "Luis Caper",
                        67,
                        "London, Straussberg",
                        Arrays.asList(new BooksDTO("The Great King", "GreyScale Publication", LocalDate.now(), "Drama"),
                                new BooksDTO("The light house and the railroad", "Harper Collins", LocalDate.of(2021, 12, 3), "Crime Drama")),
                        "The author was nominated twice for booker prize",
                        10,20,30
                ),
                new AuthorsDTO(
                        "Stephen King",
                        43,
                        "Sambhalpur",
                        Arrays.asList(new BooksDTO("The rude king", "GreyScale Publication", LocalDate.now(), "Drama"),
                                new BooksDTO("The night murder", "Harper Collins", LocalDate.of(2022, 12, 3), "Crime Drama")),
                        "The author was nominated twice for booker prize",
                        100,120,340
                ),
                new AuthorsDTO(
                        "Elara Vance",
                        35,
                        "Seattle, WA",
                        Arrays.asList(new BooksDTO("Echoes of the Void", "Cosmic Press", LocalDate.of(2024, 7, 15), "Sci-Fi"),
                                new BooksDTO("The Dragon's Ascent", "Mythos Books", LocalDate.of(2018, 5, 20), "Fantasy")),
                        "Won the Nebula Award for best debut novel in 2019.",
                        110,230,304
                ),
                new AuthorsDTO(
                        "Jane Rizzoli",
                        51,
                        "Boston, MA",
                        Arrays.asList(new BooksDTO("The Cold Witness", "Dark River Publishing", LocalDate.of(2023, 11, 28), "Thriller"),
                                new BooksDTO("Silent Screams", "Dark River Publishing", LocalDate.of(2020, 3, 15), "Mystery")),
                        "Known for her meticulous research into forensic science.",
                        101,2220,310
                ),
                new AuthorsDTO(
                        "Chloe Hayes",
                        29,
                        "Savannah, GA",
                        Arrays.asList(new BooksDTO("A Summer Fling in Santorini", "Heartwood Romance", LocalDate.of(2024, 6, 1), "Contemporary Romance"),
                                new BooksDTO("Winds of Change", "Heartwood Romance", LocalDate.of(2022, 8, 12), "Adult Fiction")),
                        "A rising star in the contemporary romance genre, with over 5 million copies sold.",
                        5504,3203,1140
                ),
                new AuthorsDTO(
                        "Dr. Anya Sharma",
                        55,
                        "Palo Alto, CA",
                        Arrays.asList(new BooksDTO("The Quantum Universe: A Beginner's Guide", "Science Focus", LocalDate.of(2019, 4, 25), "Science"),
                                new BooksDTO("Decoding Human Behavior", "Science Focus", LocalDate.of(2024, 1, 30), "Psychology")),
                        "Lead researcher at the Stanford Institute of Physics and Philosophy.",
                        3323,2243,245
                ),
                new AuthorsDTO(
                        "Julian Poe",
                        40,
                        "New York, NY",
                        Arrays.asList(new BooksDTO("Sonnets of the Subway", "Literary Ink", LocalDate.of(2023, 2, 14), "Poetry"),
                                new BooksDTO("Whispers in the Rain", "Literary Ink", LocalDate.of(2021, 10, 5), "Poetry")),
                        "His work focuses on urban loneliness and existential themes.",
                        678,789,3332
                ),
                new AuthorsDTO(
                        "Mia Chen",
                        24,
                        "Vancouver, BC",
                        Arrays.asList(new BooksDTO("The Broken Compass", "Nova Youth", LocalDate.of(2023, 9, 1), "Young Adult"),
                                new BooksDTO("Starfall Academy", "Nova Youth", LocalDate.of(2024, 5, 19), "YA Fantasy")),
                        "Known for connecting with Gen Z readers through social media.",
                        464,223,675
                ),
                new AuthorsDTO(
                        "David Attenborough",
                        89,
                        "Bristol, UK",
                        Arrays.asList(new BooksDTO("Journey to the Amazon", "Wilderness Memoirs", LocalDate.of(1988, 7, 7), "Travel"),
                                new BooksDTO("Life in the Frozen North", "Wilderness Memoirs", LocalDate.of(1999, 12, 1), "Natural History")),
                        "A renowned naturalist and documentary filmmaker.",
                        3893,4959,123
                ),
                new AuthorsDTO(
                        "Lincoln Rhyme",
                        60,
                        "Chicago, IL",
                        Arrays.asList(new BooksDTO("The Bone Collector Returns", "Gritty Crime Press", LocalDate.of(2020, 1, 20), "Crime Fiction"),
                                new BooksDTO("The Devil's Maze", "Gritty Crime Press", LocalDate.of(2024, 4, 1), "Mystery")),
                        "Specializes in gritty procedural dramas and complex mysteries.",
                        994,29938,212
                ),
                new AuthorsDTO(
                        "Mary Shelley",
                        31,
                        "Geneva, Switzerland",
                        Arrays.asList(new BooksDTO("The House on Blackwood Hill", "Gothic Tales", LocalDate.of(1818, 3, 11), "Gothic Horror"),
                                new BooksDTO("Gothic Reflections", "Gothic Tales", LocalDate.of(2014, 6, 9), "Classic Literature")),
                        "A seminal figure in the horror and science fiction genres.",
                        8293,39934,122
                )
        );
    }

    public Workbook getExcelWorkbook() {
        List<AuthorsDTO> authors = dummyData();
        System.out.println("Generating workbook from annotations...");
        GenericExportRequest exportRequest = new GenericExportRequest(Collections.singletonList(authors),
                "Author",
                "Authors Report",
                "Partial");
        Workbook workbook = workBookGenerator.createWorkbook(exportRequest);
        System.out.println("Workbook created, returning to controller...");
        return workbook;
    }

    public StreamingResponseBody getCSV() {
        List<AuthorsDTO> authors = dummyData();
        System.out.println("Generating workbook from annotations...");
        GenericExportRequest exportRequest = new GenericExportRequest(Collections.singletonList(authors),
                "Author",
                "Authors Report",
                "Partial");
        StreamingResponseBody result = csvGenerator.createCsvStream(exportRequest);
        System.out.println("Workbook created, returning to controller...");
        return result;
    }

    public void callApi() {
        log.info("Starting report download...");

        // 1. Create your test data
        List<AuthorsDTO> authors = dummyData();

        // 2. Build the request object
        //    (Using the <AuthorsDTO> type hint for the builder)
        GenericExportRequest requestBody = GenericExportRequest.builder()
                .exportableData(Collections.singletonList(authors))
                .sheetName("Author")
                .fileName("Author Report")
                .activeGroup("Partial")
                .build();

        Path outputPath = Paths.get("C:\\Users\\proxy_lbbx2v4\\Downloads\\Test\\my_downloaded_author_report.xlsx");

        // 4. Make the WebClient call
        Flux<DataBuffer> fileStream = localApiClient.post()
                .uri("/api/workbook") // <-- Calling the SPECIFIC endpoint
                .accept(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        // 5. Stream the response directly to a file
        DataBufferUtils.write(fileStream, outputPath)
                .doOnSuccess(v -> log.info("File downloaded successfully to: " + outputPath.toAbsolutePath()))
                .doOnError(e -> log.error("Error downloading file: ", e))
                .block(); // Using .block() for simplicity in this example
    }
}