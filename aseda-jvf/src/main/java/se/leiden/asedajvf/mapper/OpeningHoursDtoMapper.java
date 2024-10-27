package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.OpeningHoursDto;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.OpeningHours;

@Component
public class OpeningHoursDtoMapper {

    public OpeningHoursDto toDto(OpeningHours openingHours) {
        if (openingHours == null) {
            return null;
        }
        return OpeningHoursDto.builder()
                .Id(openingHours.getOpeningId())
                .facilityId(openingHours.getFacility().getId())
                .openingTime(openingHours.getOpeningTime())
                .closingTime(openingHours.getClosingTime())
                .assignedLeaderId(openingHours.getAssignedLeader().getId())
                .build();
    }

    public OpeningHours toOpeningHours(OpeningHoursDto dto, Facility facility, Member assignedLeader) {
        return OpeningHours.builder()
                .openingId(dto.getId())
                .facility(facility)
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .assignedLeader(assignedLeader)
                .build();
    }
}
