package se.leiden.asedajvf.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import se.leiden.asedajvf.enums.BookingStatus;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoView {
    private Long id;
    private String title;
    private Facility facilityId;
    private Member memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
}
