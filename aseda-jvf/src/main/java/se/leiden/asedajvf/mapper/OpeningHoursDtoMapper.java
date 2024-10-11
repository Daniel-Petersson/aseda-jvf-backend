package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.OpeningHoursDto;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.OpeningHours;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class OpeningHoursDtoMapper {

    public OpeningHoursDto toDto(OpeningHours openingHours) {
        if (openingHours == null) {
            return null;
        }
        return OpeningHoursDto.builder()
                .facilityId(openingHours.getFacility().getId())
                .dayOfWeek(openingHours.getDayOfWeek())
                .openingTime(openingHours.getOpeningTime().toString())
                .closingTime(openingHours.getClosingTime().toString())
                .assignedLeaderId(openingHours.getAssignedLeader().getId())
                .build();
    }

    public OpeningHours toOpeningHours(OpeningHoursDto dto, Facility facility, Member assignedLeader) {
        return OpeningHours.builder()
                .facility(facility)
                .dayOfWeek(dto.getDayOfWeek())
                .openingTime(LocalTime.parse(dto.getOpeningTime(), DateTimeFormatter.ISO_TIME))
                .closingTime(LocalTime.parse(dto.getClosingTime(), DateTimeFormatter.ISO_TIME))
                .assignedLeader(assignedLeader)
                .build();
    }
}
