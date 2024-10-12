package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.FacilityAvailabilityDto;

import java.time.LocalDateTime;
import java.util.List;

public interface FacilityAvailabilityService {
    List<FacilityAvailabilityDto> getAvailabilityByFacility(int facilityId);
    void createAvailability(FacilityAvailabilityDto dto);
    void updateAvailability(int availabilityId, FacilityAvailabilityDto dto);
    void deleteAvailability(int availabilityId);
    List<FacilityAvailabilityDto> getAvailableTimeSlots(int facilityId, LocalDateTime startDate, LocalDateTime endDate);
    void checkAvailabilityForBooking(int facilityId, LocalDateTime startTime, LocalDateTime endTime);
    boolean isFacilityAvailable(int facilityId, LocalDateTime startTime, LocalDateTime endTime);
}
