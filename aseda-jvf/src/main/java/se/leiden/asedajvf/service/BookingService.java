package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDtoView registerBooking(BookingDtoForm bookingDtoForm);
    BookingDtoView updateBooking(Long bookingId, BookingDtoForm bookingDtoForm);
    BookingDtoView getBooking(Long bookingId);
    List<Booking> getAllBookings();
    boolean deleteBooking(Long bookingId);
    List<Booking> findOverlappingBookings(String facilityId, LocalDateTime startTime, LocalDateTime endTime);
    BookingDtoView confirmBooking(Long bookingId);
}
