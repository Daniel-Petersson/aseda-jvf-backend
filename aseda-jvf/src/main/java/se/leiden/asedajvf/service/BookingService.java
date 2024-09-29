package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDtoForm registerBooking(BookingDtoForm bookingDtoForm);
    BookingDtoForm updateBooking(BookingDtoForm bookingDtoForm);
    BookingDtoView getBooking(String bookingId);
    List<Booking> getAllBookings();
    boolean deleteBooking(Booking bookingId);
}
