package se.leiden.asedajvf.service;

import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.model.Facility;

import java.util.List;

public interface FacilityService {
    List<Facility> getAllFacilities();
    Facility getFacilityById(int facilityId)throws ResourceNotFoundException;
    Facility getFacilityByFacilityName(String facilityName)throws ResourceNotFoundException;
}
