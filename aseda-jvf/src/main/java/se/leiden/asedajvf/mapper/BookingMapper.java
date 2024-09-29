package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.model.Booking;

@Component
public class BookingMapper {
    public static BookingDtoView toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoView.builder()
                .id(booking.getId())
                .title(booking.getTitle())
                .facilityId(booking.getFacilityId())
                .memberId(booking.getMemberId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .build();
    }
    public static Booking toBooking(BookingDtoForm bookingDtoForm) {
        return Booking.builder()
                .title(bookingDtoForm.getTitle())
                .facilityId(bookingDtoForm.getFacilityId())
                .memberId(bookingDtoForm.getMemberId())
                .startTime(bookingDtoForm.getStartTime())
                .endTime(bookingDtoForm.getEndTime())
                .build();
    }
}
