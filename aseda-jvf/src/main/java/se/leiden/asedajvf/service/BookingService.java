package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.exeptions.UnauthorizedException;
import se.leiden.asedajvf.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDtoView registerBooking(BookingDtoForm bookingDtoForm) throws IllegalArgumentException;
    BookingDtoView updateBooking(int bookingId, BookingDtoForm bookingDtoForm, String token) throws UnauthorizedException;
    BookingDtoView getBooking(int bookingId);
    List<BookingDtoView> getAllBookings();
    boolean deleteBooking(int bookingId, String token) throws UnauthorizedException;
    List<Booking> findOverlappingBookings(int facilityId, LocalDateTime startTime, LocalDateTime endTime);
    BookingDtoView confirmBooking(int bookingId);
    boolean isFacilityAvailable(int facilityId, LocalDateTime startTime, LocalDateTime endTime);
}
