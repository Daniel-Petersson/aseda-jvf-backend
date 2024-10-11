package se.leiden.asedajvf.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpeningHoursDto {
    private int facilityId;
    private String dayOfWeek;
    private String openingTime; // ISO 8601 format
    private String closingTime; // ISO 8601 format
    private int assignedLeaderId;
}
