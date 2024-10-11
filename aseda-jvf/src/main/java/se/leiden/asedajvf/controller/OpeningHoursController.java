package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.OpeningHoursDto;
import se.leiden.asedajvf.service.OpeningHoursService;

import java.util.List;

@RestController
@RequestMapping("/api/opening-hours/")
public class OpeningHoursController {

    private final OpeningHoursService openingHoursService;

    public OpeningHoursController(OpeningHoursService openingHoursService) {
        this.openingHoursService = openingHoursService;
    }

    @Operation(summary = "Create new opening hours", description = "Creates new opening hours for a facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Opening hours created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Void> createOpeningHours(@RequestBody OpeningHoursDto dto) {
        openingHoursService.createOpeningHours(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update existing opening hours", description = "Updates opening hours by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opening hours updated successfully"),
            @ApiResponse(responseCode = "404", description = "Opening hours not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> updateOpeningHours(@PathVariable int id, @RequestBody OpeningHoursDto dto) {
        openingHoursService.updateOpeningHours(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete opening hours", description = "Deletes opening hours by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Opening hours deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Opening hours not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOpeningHours(@PathVariable int id) {
        openingHoursService.deleteOpeningHours(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get opening hours by facility ID", description = "Retrieves opening hours by facility ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved opening hours")
    })
    @GetMapping("{facilityId}")
    public ResponseEntity<List<OpeningHoursDto>> getOpeningHoursByFacility(@PathVariable int facilityId) {
        List<OpeningHoursDto> openingHours = openingHoursService.getOpeningHoursByFacility(facilityId);
        return ResponseEntity.ok(openingHours);
    }
}