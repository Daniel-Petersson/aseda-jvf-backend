package se.leiden.asedajvf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.service.FacilityService;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {
    private final FacilityService facilityService;
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable int id) throws ResourceNotFoundException {
       Facility facility = facilityService.getFacilityById(id);
       return ResponseEntity.ok(facility);
    }

    @GetMapping("/facilityName")
    public ResponseEntity<Facility> getFacilitiesByFacilityName(@RequestParam String facilityName) throws ResourceNotFoundException {
        Facility facilities= facilityService.getFacilityByFacilityName(facilityName);
        return ResponseEntity.ok(facilities);
    }
}
