package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.FacilityAvailabilityDto;
import se.leiden.asedajvf.service.FacilityAvailabilityService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facility-availability/")
public class FacilityAvailabilityController {

    private final FacilityAvailabilityService facilityAvailabilityService;

    public FacilityAvailabilityController(FacilityAvailabilityService facilityAvailabilityService) {
        this.facilityAvailabilityService = facilityAvailabilityService;
    }

    @Operation(summary = "Create a new facility availability", description = "Creates a new facility availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facility availability created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Void> createAvailability(@RequestBody FacilityAvailabilityDto dto) {
        facilityAvailabilityService.createAvailability(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update an existing facility availability", description = "Updates a facility availability by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility availability updated successfully"),
            @ApiResponse(responseCode = "404", description = "Facility availability not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> updateAvailability(@PathVariable int id, @RequestBody FacilityAvailabilityDto dto) {
        facilityAvailabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a facility availability", description = "Deletes a facility availability by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Facility availability deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Facility availability not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable int id) {
        facilityAvailabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get facility availability by facility ID", description = "Retrieves facility availability by facility ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved facility availability")
    })
    @GetMapping("{facilityId}")
    public ResponseEntity<List<FacilityAvailabilityDto>> getAvailabilityByFacility(@PathVariable int facilityId) {
        List<FacilityAvailabilityDto> availabilities = facilityAvailabilityService.getAvailabilityByFacility(facilityId);
        return ResponseEntity.ok(availabilities);
    }

    @Operation(summary = "Check if a facility is available", description = "Checks if a facility is available for a specific time range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked facility availability")
    })
    @GetMapping("check")
    public ResponseEntity<Boolean> isFacilityAvailable(
            @RequestParam int facilityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean isAvailable = facilityAvailabilityService.isFacilityAvailable(facilityId, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Get available time slots", description = "Retrieves available time slots for a facility within a given date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available time slots")
    })
    @GetMapping("slots")
    public ResponseEntity<List<FacilityAvailabilityDto>> getAvailableTimeSlots(
            @RequestParam int facilityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<FacilityAvailabilityDto> availableSlots = facilityAvailabilityService.getAvailableTimeSlots(facilityId, startDate, endDate);
        return ResponseEntity.ok(availableSlots);
    }
}
