package se.leiden.asedajvf.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.mapper.BookingMapper;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.BookingRepository;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.service.BookingServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBooking_Success() {
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Facility facility = new Facility();
        Member member = new Member();
        Booking booking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(facilityRepository.findById(1)).thenReturn(Optional.of(facility));
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(bookingRepository.findOverlappingBookings(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(bookingMapper.toBooking(any(BookingDtoForm.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expectedView);

        BookingDtoView result = bookingService.registerBooking(form);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_Success() {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Booking existingBooking = new Booking();
        Facility facility = new Facility();
        Member member = new Member();
        Booking updatedBooking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facility));
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(bookingMapper.toBooking(any(BookingDtoForm.class))).thenReturn(updatedBooking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expectedView);

        BookingDtoView result = bookingService.updateBooking(bookingId, form);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getBooking_Success() {
        int bookingId = 1;
        Booking booking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(expectedView);

        BookingDtoView result = bookingService.getBooking(bookingId);

        assertNotNull(result);
        assertEquals(expectedView, result);
    }

    @Test
    void getBooking_NotFound() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.getBooking(bookingId));
    }

    @Test
    void deleteBooking_Success() {
        int bookingId = 1;
        Booking booking = new Booking();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        boolean result = bookingService.deleteBooking(bookingId);

        assertTrue(result);
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBooking_NotFound() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.deleteBooking(bookingId));
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void getAllBookings_Success() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());

        List<BookingDtoView> expectedDtoViews = new ArrayList<>();
        expectedDtoViews.add(new BookingDtoView());
        expectedDtoViews.add(new BookingDtoView());

        when(bookingRepository.findAll()).thenReturn(bookings);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(new BookingDtoView());

        List<BookingDtoView> result = bookingService.getAllBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository).findAll();
        verify(bookingMapper, times(2)).toDto(any(Booking.class));
    }

    @Test
    void findOverlappingBookings_Success() {
        int facilityId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(new Booking());

        when(bookingRepository.findOverlappingBookings(facilityId, startTime, endTime)).thenReturn(expectedBookings);

        List<Booking> result = bookingService.findOverlappingBookings(facilityId, startTime, endTime);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void confirmBooking_Success() {
        int bookingId = 1;
        Booking booking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(expectedView);

        BookingDtoView result = bookingService.confirmBooking(bookingId);

        assertNotNull(result);
        assertEquals(expectedView, result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void confirmBooking_NotFound() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.confirmBooking(bookingId));
    }
}