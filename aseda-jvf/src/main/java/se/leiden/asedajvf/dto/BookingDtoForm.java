package se.leiden.asedajvf.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import se.leiden.asedajvf.enums.BookingStatus;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoForm {
    @NotBlank(message = "Title required")
    private String title;
    @NotNull(message = "Facility id required")
    private Facility facilityId;
    @NotNull(message = "Member id required")
    private Member memberId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;
    //Might not be needed in dtoform
    //private BookingStatus status;
}
