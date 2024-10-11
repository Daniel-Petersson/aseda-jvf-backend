package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.FacilityDto;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;

import java.util.List;

public interface FacilityService {
    List<FacilityDto> getAllFacilities();
    FacilityDto getFacilityById(int facilityId)throws ResourceNotFoundException;
    FacilityDto getFacilityByFacilityName(String facilityName)throws ResourceNotFoundException;
}
