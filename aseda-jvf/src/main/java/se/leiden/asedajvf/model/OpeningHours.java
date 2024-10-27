package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int openingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    private LocalDateTime openingTime;
    private LocalDateTime closingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_leader_id")
    private Member assignedLeader;

}