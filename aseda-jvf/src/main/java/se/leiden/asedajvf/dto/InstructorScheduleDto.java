package se.leiden.asedajvf.dto;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
public class InstructorScheduleDto {
    private Integer id;

    @NotNull(message = "Instructor ID is required")
    private Integer instructorId;

    @NotNull(message = "Facility ID is required")
    private Integer facilityId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
}
