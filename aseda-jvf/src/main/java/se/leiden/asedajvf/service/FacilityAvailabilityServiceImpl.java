package se.leiden.asedajvf.service;

import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.FacilityAvailabilityDto;
import se.leiden.asedajvf.mapper.FacilityAvailabilityMapper;
import se.leiden.asedajvf.model.FacilityAvailability;
import se.leiden.asedajvf.repository.FacilityAvailabilityRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityAvailabilityServiceImpl implements FacilityAvailabilityService {
    private final FacilityAvailabilityRepository repository;
    private final FacilityAvailabilityMapper mapper;


    public FacilityAvailabilityServiceImpl(FacilityAvailabilityRepository repository, FacilityAvailabilityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    @Override
    public List<FacilityAvailabilityDto> getAvailabilityByFacility(int facilityId) {
        return repository.findByFacilityId(facilityId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createAvailability(FacilityAvailabilityDto dto) {
        validateAvailability(dto);
        FacilityAvailability availability = mapper.toEntity(dto);
        repository.save(availability);
    }

    @Override
    public void updateAvailability(int availabilityId, FacilityAvailabilityDto dto) {
        validateAvailability(dto);
        FacilityAvailability availability = repository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid availability ID"));
        mapper.updateEntityFromDto(availability, dto);
        repository.save(availability);
    }

    @Override
    public void deleteAvailability(int availabilityId) {
        repository.deleteById(availabilityId);
    }

  

    @Override
    public List<FacilityAvailabilityDto> getAvailableTimeSlots(int facilityId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByFacilityIdAndStartTimeBetween(facilityId, startDate, endDate).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void checkAvailabilityForBooking(int facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!isFacilityAvailable(facilityId, startTime, endTime)) {
            throw new IllegalArgumentException("The facility is not available for the requested time slot");
        }
    }

    @Override
    public boolean isFacilityAvailable(int facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        
        List<FacilityAvailability> availabilities = repository.findByFacilityId(facilityId);
        
        for (FacilityAvailability availability : availabilities) {
            if (availability.getStartTime().isBefore(endTime) && availability.getEndTime().isAfter(startTime)) {
                return true; // Found an availability that overlaps with the requested time range
            }
        }
        
        return false; // No availability overlaps with the requested time range
    }

    private void validateAvailability(FacilityAvailabilityDto dto) {
        LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endTime = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME);

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        List<FacilityAvailability> overlappingAvailabilities = repository.findOverlappingAvailabilities(
                dto.getFacilityId(), startTime, endTime);

        if (!overlappingAvailabilities.isEmpty()) {
            throw new IllegalArgumentException("The new availability overlaps with existing availabilities");
        }
    }

}
