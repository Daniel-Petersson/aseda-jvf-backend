package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.FacilityAvailabilityDto;
import se.leiden.asedajvf.model.FacilityAvailability;
import se.leiden.asedajvf.repository.FacilityRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FacilityAvailabilityMapper {

    private final FacilityRepository facilityRepository;

    public FacilityAvailabilityMapper(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public FacilityAvailabilityDto toDto(FacilityAvailability entity) {
        if (entity == null) {
            return null;
        }

        return FacilityAvailabilityDto.builder()
                .facilityId(entity.getFacility().getId())
                .startTime(entity.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME))
                .endTime(entity.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME))
                .seasonal(entity.isSeasonal())
                .build();
    }

    public FacilityAvailability toEntity(FacilityAvailabilityDto dto) {
        if (dto == null) {
            return null;
        }

        FacilityAvailability entity = new FacilityAvailability();
        updateEntityFromDto(entity, dto);
        return entity;
    }

    public void updateEntityFromDto(FacilityAvailability entity, FacilityAvailabilityDto dto) {
        entity.setFacility(facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID")));
        entity.setStartTime(LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        entity.setEndTime(LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        entity.setSeasonal(dto.isSeasonal());
    }
}