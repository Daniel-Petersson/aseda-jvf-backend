package se.leiden.asedajvf.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpeningHoursDto {
    private int facilityId;
    private LocalDateTime openingTime; 
    private LocalDateTime closingTime; 
    private int assignedLeaderId;
}
