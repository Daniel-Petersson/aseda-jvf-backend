package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.NewsDtoForm;
import se.leiden.asedajvf.dto.NewsDtoView;
import se.leiden.asedajvf.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Create a news entry", description = "Creates a new news entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    public ResponseEntity<NewsDtoView> createNews(@RequestBody @Valid NewsDtoForm newsDtoForm) {
        NewsDtoView responseBody = newsService.createNews(newsDtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @Operation(summary = "Update a news entry", description = "Updates an existing news entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News updated successfully"),
            @ApiResponse(responseCode = "404", description = "News not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsDtoView> updateNews(@PathVariable int id, @RequestBody @Valid NewsDtoForm newsDtoForm) {
        NewsDtoView responseBody = newsService.updateNews(id, newsDtoForm);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Delete a news entry", description = "Deletes a news entry by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "News deleted successfully"),
            @ApiResponse(responseCode = "404", description = "News not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable int id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a news entry", description = "Gets a news entry by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "News not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDtoView> getNewsById(@PathVariable int id) {
        NewsDtoView responseBody = newsService.getNewsById(id);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Get all news entries", description = "Gets all news entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News retrieved successfully")
    })
    @GetMapping("/")
    public ResponseEntity<List<NewsDtoView>> getAllNews() {
        List<NewsDtoView> responseBody = newsService.getAllNews();
        return ResponseEntity.ok(responseBody);
    }
}
