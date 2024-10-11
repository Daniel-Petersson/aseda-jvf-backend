package se.leiden.asedajvf.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacilityAvailabilityDto {
    private int facilityId;
    private String startTime; // ISO 8601 format
    private String endTime;   // ISO 8601 format
    private boolean seasonal;
}
