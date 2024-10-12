package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.InstructorScheduleDto;
import se.leiden.asedajvf.service.InstructorScheduleService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/instructor-schedules")
public class InstructorScheduleController {
    // TODO: Implement token-based authentication and authorization for all endpoints

    private final InstructorScheduleService instructorScheduleService;

    public InstructorScheduleController(InstructorScheduleService instructorScheduleService) {
        this.instructorScheduleService = instructorScheduleService;
    }

    @Operation(summary = "Create a new instructor schedule", description = "Creates a new instructor schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instructor schedule created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<InstructorScheduleDto> createSchedule(@Valid @RequestBody InstructorScheduleDto dto) {
        InstructorScheduleDto createdSchedule = instructorScheduleService.createSchedule(dto);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an instructor schedule", description = "Updates an existing instructor schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor schedule updated successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor schedule not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InstructorScheduleDto> updateSchedule(@PathVariable Integer id, @Valid @RequestBody InstructorScheduleDto dto) {
        InstructorScheduleDto updatedSchedule = instructorScheduleService.updateSchedule(id, dto);
        return ResponseEntity.ok(updatedSchedule);
    }

    @Operation(summary = "Delete an instructor schedule", description = "Deletes an instructor schedule by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Instructor schedule deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor schedule not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        boolean deleted = instructorScheduleService.deleteSchedule(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get an instructor schedule by ID", description = "Retrieves an instructor schedule by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor schedule retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor schedule not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InstructorScheduleDto> getScheduleById(@PathVariable Integer id) {
        InstructorScheduleDto schedule = instructorScheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = "Get schedules by instructor", description = "Retrieves all schedules for a specific instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    })
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<InstructorScheduleDto>> getSchedulesByInstructor(@PathVariable Integer instructorId) {
        List<InstructorScheduleDto> schedules = instructorScheduleService.getSchedulesByInstructor(instructorId);
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Get schedules by facility", description = "Retrieves all schedules for a specific facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    })
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<InstructorScheduleDto>> getSchedulesByFacility(@PathVariable Integer facilityId) {
        List<InstructorScheduleDto> schedules = instructorScheduleService.getSchedulesByFacility(facilityId);
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Get all instructor schedules", description = "Retrieves all instructor schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<InstructorScheduleDto>> getAllSchedules() {
        List<InstructorScheduleDto> schedules = instructorScheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }
}