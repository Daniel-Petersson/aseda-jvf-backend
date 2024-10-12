package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.exeptions.UnauthorizedException;
import se.leiden.asedajvf.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a new booking", description = "Creates a new booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "The facility is not available for the requested time slot")
    })
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDtoForm bookingDtoForm) {
        try {
            BookingDtoView bookingDtoView = bookingService.registerBooking(bookingDtoForm);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingDtoView);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Update an existing booking", description = "Updates a booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<BookingDtoView> updateBooking(
        @PathVariable int id,
        @RequestBody BookingDtoForm bookingDtoForm,
        @RequestHeader("Authorization") String token) throws UnauthorizedException {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        BookingDtoView bookingDtoView = bookingService.updateBooking(id, bookingDtoForm, token);
        return ResponseEntity.ok(bookingDtoView);
    }

    @Operation(summary = "Get a booking by Id", description = "Retrieves a booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved booking"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<BookingDtoView> getBooking(@PathVariable int id) {
        BookingDtoView bookingDtoView = bookingService.getBooking(id);
        return ResponseEntity.ok(bookingDtoView);
    }

    @Operation(summary = "View a list of all bookings", description = "Retrieves all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<BookingDtoView>> getAllBookings() {
        List<BookingDtoView> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Delete a booking", description = "Deletes a booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBooking(
        @PathVariable int id,
        @RequestHeader("Authorization") String token) throws UnauthorizedException {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        bookingService.deleteBooking(id, token);
        return ResponseEntity.noContent().build();
    }
}
