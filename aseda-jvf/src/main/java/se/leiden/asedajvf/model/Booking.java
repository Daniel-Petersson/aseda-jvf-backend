package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import lombok.*;
import se.leiden.asedajvf.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
