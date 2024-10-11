package se.leiden.asedajvf.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacilityDto {
    private int id;
    private String name;
    private String description;
    private List<BookingDtoView> bookings;

}
