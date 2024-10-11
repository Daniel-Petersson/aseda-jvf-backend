package se.leiden.asedajvf.service;

import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.FacilityAvailabilityDto;
import se.leiden.asedajvf.model.FacilityAvailability;
import se.leiden.asedajvf.repository.FacilityAvailabilityRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityAvailabilityServiceImpl implements FacilityAvailabilityService {
    private final FacilityAvailabilityRepository repository;

    public FacilityAvailabilityServiceImpl(FacilityAvailabilityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FacilityAvailabilityDto> getAvailabilityByFacility(int facilityId) {
        return repository.findByFacilityId(facilityId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createAvailability(FacilityAvailabilityDto dto) {
        FacilityAvailability availability = new FacilityAvailability();
        availability.setStartTime(LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        availability.setEndTime(LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        availability.setSeasonal(dto.isSeasonal());
        repository.save(availability);
    }

    @Override
    public void updateAvailability(int availabilityId, FacilityAvailabilityDto dto) {
        FacilityAvailability availability = repository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid availability ID"));
        availability.setStartTime(LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        availability.setEndTime(LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        availability.setSeasonal(dto.isSeasonal());
        repository.save(availability);
    }

    @Override
    public void deleteAvailability(int availabilityId) {
        repository.deleteById(availabilityId);
    }

    private FacilityAvailabilityDto toDto(FacilityAvailability availability) {
        return new FacilityAvailabilityDto(
                availability.getAvailabilityId(),
                availability.getStartTime().toString(),
                availability.getEndTime().toString(),
                availability.isSeasonal()
        );
    }
}
