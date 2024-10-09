package se.leiden.asedajvf.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.enums.BookingStatus;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.mapper.BookingMapper;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.repository.BookingRepository;
import se.leiden.asedajvf.repository.MemberRepository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final MemberRepository memberRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper; // Add this line

    @Override
    @Transactional
    public BookingDtoView registerBooking(BookingDtoForm bookingDtoForm) {
        if (bookingDtoForm == null) {
            throw new IllegalArgumentException("bookingDtoForm is null");
        }
        
        List<Booking> conflictingBookings = findOverlappingBookings(
            bookingDtoForm.getFacilityId(), // Use the integer directly
            bookingDtoForm.getStartTime(),
            bookingDtoForm.getEndTime()
        );
        
        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("Conflicting booking exists for the given time and facility");
        }
        
        Booking booking = bookingMapper.toBooking(bookingDtoForm);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDtoView updateBooking(int bookingId, BookingDtoForm bookingDtoForm) {
        if (bookingDtoForm == null) {
            throw new IllegalArgumentException("bookingDtoForm is null");
        }
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new DataNotFoundException("Booking not found"));
        
        // Update booking fields
        Booking updatedBooking = bookingMapper.toBooking(bookingDtoForm);
        updatedBooking.setId(bookingId);
        updatedBooking.setStatus(booking.getStatus());
        
        Booking savedBooking = bookingRepository.save(updatedBooking);
        return bookingMapper.toDto(savedBooking); // Use instance method
    }

    @Override
    public BookingDtoView getBooking(int bookingId) {
        return bookingRepository.findById(bookingId)
                .map(bookingMapper::toDto) // Use instance method
                .orElseThrow(() -> new DataNotFoundException("Booking with id: " + bookingId + " does not exist"));
    }

    @Override
    public List<BookingDtoView> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteBooking(int id) {
        return bookingRepository.findById(id)
            .map(booking -> {
                bookingRepository.delete(booking);
                return true;
            })
            .orElseThrow(() -> new DataNotFoundException("Booking with id: " + id + " does not exist"));
    }

    @Override
    public List<Booking> findOverlappingBookings(int facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime must not be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        return bookingRepository.findOverlappingBookings(facilityId, startTime, endTime);
    }

    @Override
    @Transactional
    public BookingDtoView confirmBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking); // Use instance method
    }

    // TODO: Add more robust input validation, especially for date ranges and facility availability
    // TODO: Implement a mechanism to handle near-simultaneous booking attempts for the same time slot
    // TODO: Consider adding checks for maximum booking duration or frequency per member
    // TODO: Implement rules for when bookings can be cancelled or modified
    // TODO: Add support for recurring bookings if needed
    // TODO: Implement a notification system for booking confirmations, reminders, and changes
    // TODO: Consider adding a waiting list feature for popular time slots
}
