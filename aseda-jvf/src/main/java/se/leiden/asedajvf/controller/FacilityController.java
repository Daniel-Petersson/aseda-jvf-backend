package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.FacilityDto;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.service.FacilityService;

import java.util.List;

@RestController
@RequestMapping("/api/facility/")
public class FacilityController {
    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @Operation(summary = "View a list of available facilities", description = "Retrieves all facilities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<FacilityDto>> getAllFacilities() {
        List<FacilityDto> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    @Operation(summary = "Get a facility by Id", description = "Retrieves a facility by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved facility"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<FacilityDto> getFacilityById(@PathVariable int id) throws ResourceNotFoundException {
       FacilityDto facilityDto = facilityService.getFacilityById(id);
       return ResponseEntity.status(HttpStatus.OK).body(facilityDto);
    }

    @Operation(summary = "Get a facility by name", description = "Retrieves a facility by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved facility"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping("facilityName")
    public ResponseEntity<FacilityDto> getFacilitiesByFacilityName(@RequestParam String facilityName) throws ResourceNotFoundException {
        FacilityDto facilityDto= facilityService.getFacilityByFacilityName(facilityName);
        return ResponseEntity.status(HttpStatus.OK).body(facilityDto);
    }
}
