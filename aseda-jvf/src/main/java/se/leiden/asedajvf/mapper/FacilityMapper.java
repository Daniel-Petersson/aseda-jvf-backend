package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.FacilityDto;
import se.leiden.asedajvf.model.Facility;
@Component
public class FacilityMapper {
   

    public FacilityDto toDto(Facility facility) {
        if (facility==null){
            return null;
        }
        return FacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .description(facility.getDescription())
                .build();
    }

    public Facility toFacility(FacilityDto facilityDto) {
        return Facility.builder()
                .id(facilityDto.getId())
                .name(facilityDto.getName())
                .description(facilityDto.getDescription())
                .build();
    }
}


