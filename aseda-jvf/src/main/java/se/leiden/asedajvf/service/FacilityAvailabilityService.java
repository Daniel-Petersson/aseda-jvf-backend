package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.FacilityAvailabilityDto;
import se.leiden.asedajvf.model.FacilityAvailability;

import java.util.List;

public interface FacilityAvailabilityService {
    List<FacilityAvailabilityDto> getAvailabilityByFacility(int facilityId);
    void createAvailability(FacilityAvailabilityDto dto);
    void updateAvailability(int availabilityId, FacilityAvailabilityDto dto);
    void deleteAvailability(int availabilityId);
}
