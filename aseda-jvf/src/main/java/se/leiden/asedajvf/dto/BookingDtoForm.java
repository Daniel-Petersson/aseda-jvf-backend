package se.leiden.asedajvf.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


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
    private int facilityId;
    @NotNull(message = "Member id required")
    private int memberId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;
    //Might not be needed in dtoform
    //private BookingStatus status;
}
