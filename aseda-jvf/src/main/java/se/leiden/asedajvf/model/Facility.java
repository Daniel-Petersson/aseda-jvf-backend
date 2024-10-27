package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 50, message = "Description must be between 2 and 50 characters")
    private String description;
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OpeningHours> openingHours = new ArrayList<>();
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FacilityAvailability> availabilities = new ArrayList<>();
    @OneToMany(mappedBy = "facility")
    private List<InstructorSchedule> instructorSchedules;
}
