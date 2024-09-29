package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import se.leiden.asedajvf.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Title required")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
    private String title;
    @ManyToOne
    @JoinColumn(name = "facility", nullable = false)
    private Facility facilityId;
    @ManyToOne
    @JoinColumn(name = "member", nullable = false)
    private Member memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
