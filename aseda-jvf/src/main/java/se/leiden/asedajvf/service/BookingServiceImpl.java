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

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final MemberRepository memberRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDtoView registerBooking(BookingDtoForm bookingDtoForm) {
        if (bookingDtoForm == null) {
            throw new IllegalArgumentException("bookingDtoForm is null");
        }
        
        List<Booking> conflictingBookings = findOverlappingBookings(
            bookingDtoForm.getFacilityId().getId().toString(), 
            bookingDtoForm.getStartTime(),
            bookingDtoForm.getEndTime()
        );
        
        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("Conflicting booking exists for the given time and facility");
        }
        
        Booking booking = BookingMapper.toBooking(bookingDtoForm);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDtoView updateBooking(Long bookingId, BookingDtoForm bookingDtoForm) {
        if (bookingDtoForm == null) {
            throw new IllegalArgumentException("bookingDtoForm is null");
        }
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new DataNotFoundException("Booking not found"));
        
        // Update booking fields
        booking.setTitle(bookingDtoForm.getTitle());
        booking.setFacilityId(bookingDtoForm.getFacilityId());
        booking.setMemberId(bookingDtoForm.getMemberId());
        booking.setStartTime(bookingDtoForm.getStartTime());
        booking.setEndTime(bookingDtoForm.getEndTime());
        
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(updatedBooking);
    }

    @Override
    public BookingDtoView getBooking(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("bookingId is null");
        }
        return bookingRepository.findById(bookingId)
                .map(BookingMapper::toDto)
                .orElseThrow(() -> new DataNotFoundException("Booking with id: " + bookingId + " does not exist"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return (List<Booking>) bookingRepository.findAll();
    }

    @Override
    @Transactional
    public boolean deleteBooking(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("bookingId is null");
        }
        if (!bookingRepository.existsById(bookingId)) {
            throw new DataNotFoundException("Booking with id: " + bookingId + " does not exist");
        }
        bookingRepository.deleteById(bookingId);
        return true;
    }

    @Override
    public List<Booking> findOverlappingBookings(String facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        if (facilityId == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("facilityId, startTime, and endTime must not be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        return bookingRepository.findOverlappingBookings(facilityId, startTime, endTime);
    }

    @Override
    @Transactional
    public BookingDtoView confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    // TODO: Add more robust input validation, especially for date ranges and facility availability
    // TODO: Implement a mechanism to handle near-simultaneous booking attempts for the same time slot
    // TODO: Consider adding checks for maximum booking duration or frequency per member
    // TODO: Implement rules for when bookings can be cancelled or modified
    // TODO: Add support for recurring bookings if needed
    // TODO: Implement a notification system for booking confirmations, reminders, and changes
    // TODO: Consider adding a waiting list feature for popular time slots
}
