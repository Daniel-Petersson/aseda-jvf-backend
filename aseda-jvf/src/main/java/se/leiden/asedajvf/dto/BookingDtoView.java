package se.leiden.asedajvf.dto;


import lombok.*;
import se.leiden.asedajvf.enums.BookingStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoView {
    private int id;
    private String title;
    private int facilityId;
    private int memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
}
