package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.model.Booking;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    @Override
    public BookingDtoForm registerBooking(BookingDtoForm bookingDtoForm) {
        return null;
    }

    @Override
    public BookingDtoForm updateBooking(BookingDtoForm bookingDtoForm) {
        return null;
    }

    @Override
    public BookingDtoView getBooking(String bookingId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        return List.of();
    }

    @Override
    public boolean deleteBooking(Booking bookingId) {
        return false;
    }
}
