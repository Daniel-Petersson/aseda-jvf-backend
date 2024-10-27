package se.leiden.asedajvf.bookingTest;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.exeptions.UnauthorizedException;
import se.leiden.asedajvf.mapper.BookingMapper;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.BookingRepository;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.service.BookingServiceImpl;
import se.leiden.asedajvf.service.JwtService;
import se.leiden.asedajvf.service.FacilityAvailabilityService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private FacilityAvailabilityService facilityAvailabilityService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() throws UnauthorizedException {
        MockitoAnnotations.openMocks(this);
        
        // Mocking JwtService to return a valid Claims object
        Claims claims = mock(Claims.class);
        when(claims.get("memberId", Integer.class)).thenReturn(1); // Ensure this matches the member ID in the booking
        when(claims.get("role", String.class)).thenReturn("USER");
        
        when(jwtService.validateTokenAndGetClaims(anyString())).thenReturn(claims);
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
        doNothing().when(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());

        BookingDtoView result = bookingService.registerBooking(form);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
        verify(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        verify(bookingRepository).findOverlappingBookings(anyInt(), any(), any());
    }

    @Test
    void registerBooking_FacilityNotAvailable() {
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

doThrow(new IllegalArgumentException("Facility not available"))
    .when(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        assertThrows(IllegalArgumentException.class, () -> bookingService.registerBooking(form));
        verify(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void registerBooking_OverlappingBookings() {
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        doNothing().when(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());        when(bookingRepository.findOverlappingBookings(anyInt(), any(), any())).thenReturn(List.of(new Booking()));

        assertThrows(IllegalArgumentException.class, () -> bookingService.registerBooking(form));
        verify(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        verify(bookingRepository).findOverlappingBookings(anyInt(), any(), any());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void isFacilityAvailable_True() {
        int facilityId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        when(facilityAvailabilityService.isFacilityAvailable(facilityId, startTime, endTime)).thenReturn(true);

        boolean result = bookingService.isFacilityAvailable(facilityId, startTime, endTime);

        assertTrue(result);
        verify(facilityAvailabilityService).isFacilityAvailable(facilityId, startTime, endTime);
    }

    @Test
    void isFacilityAvailable_False() {
        int facilityId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        when(facilityAvailabilityService.isFacilityAvailable(facilityId, startTime, endTime)).thenReturn(false);

        boolean result = bookingService.isFacilityAvailable(facilityId, startTime, endTime);

        assertFalse(result);
        verify(facilityAvailabilityService).isFacilityAvailable(facilityId, startTime, endTime);
    }

    @Test
    void updateBooking_Success() throws UnauthorizedException {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Booking existingBooking = new Booking();
        Member member = new Member();
        member.setId(1); // Set member ID
        existingBooking.setMember(member); // Set member in booking

        Facility facility = new Facility();
        Booking updatedBooking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facility));
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(bookingMapper.toBooking(any(BookingDtoForm.class))).thenReturn(updatedBooking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expectedView);

        String token = "validToken"; // Mock or create a valid token

        BookingDtoView result = bookingService.updateBooking(bookingId, form, token);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_AdminUser() throws UnauthorizedException {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(2); // Different memberId to simulate another user
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Booking existingBooking = new Booking();
        Member member = new Member();
        member.setId(1); // Original member ID
        existingBooking.setMember(member);

        Booking updatedBooking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingMapper.toBooking(any(BookingDtoForm.class))).thenReturn(updatedBooking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expectedView);

        // Mock the token for admin
        Claims claims = mock(Claims.class);
        when(claims.get("memberId", Integer.class)).thenReturn(2); // Different memberId
        when(claims.get("role", String.class)).thenReturn("ADMIN");
        when(jwtService.validateTokenAndGetClaims(anyString())).thenReturn(claims);

        String token = "adminToken"; // Mock or create a valid admin token

        BookingDtoView result = bookingService.updateBooking(bookingId, form, token);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_AuthorizedUser() throws UnauthorizedException {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Booking existingBooking = new Booking();
        Member member = new Member();
        member.setId(1); // Set member ID
        existingBooking.setMember(member); // Set member in booking

        Booking updatedBooking = new Booking();
        BookingDtoView expectedView = new BookingDtoView();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingMapper.toBooking(any(BookingDtoForm.class))).thenReturn(updatedBooking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expectedView);

        String token = "validToken"; // Mock or create a valid token

        BookingDtoView result = bookingService.updateBooking(bookingId, form, token);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_UnauthorizedUser() {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(2); // Different memberId
        form.setStartTime(LocalDateTime.now());
        form.setEndTime(LocalDateTime.now().plusHours(1));

        Booking existingBooking = new Booking();
        existingBooking.setMember(new Member()); // Use a constructor that exists

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        String token = "invalidToken"; // Mock or create an invalid token

        assertThrows(UnauthorizedException.class, () -> bookingService.updateBooking(bookingId, form, token));
    }

    @Test
    void updateBooking_Unauthorized() {
        int bookingId = 1;
        BookingDtoForm form = new BookingDtoForm();
        Booking existingBooking = new Booking();
        Member member = new Member();
        member.setId(2); // Different member ID
        existingBooking.setMember(member);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        String token = "validToken";

        assertThrows(UnauthorizedException.class, () -> bookingService.updateBooking(bookingId, form, token));
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
    void deleteBooking_Success() throws UnauthorizedException {
        int bookingId = 1;
        Booking booking = new Booking();
        Member member = new Member();
        member.setId(1);
        booking.setMember(member);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        String token = "validToken";

        boolean result = bookingService.deleteBooking(bookingId, token);

        assertTrue(result);
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBooking_AuthorizedUser() throws UnauthorizedException {
        int bookingId = 1;
        Booking booking = new Booking();
        Member member = new Member();
        member.setId(1); // Set member ID
        booking.setMember(member); // Set member in booking

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        String token = "validToken"; // Mock or create a valid token

        boolean result = bookingService.deleteBooking(bookingId, token);

        assertTrue(result);
        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBooking_AdminUser() throws UnauthorizedException {
        int bookingId = 1;
        Booking booking = new Booking();
        Member member = new Member();
        member.setId(1);
        booking.setMember(member);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        // Mock token for admin
        Claims adminClaims = mock(Claims.class);
        when(adminClaims.get("memberId", Integer.class)).thenReturn(2);
        when(adminClaims.get("role", String.class)).thenReturn("ADMIN");
        when(jwtService.validateTokenAndGetClaims("adminToken")).thenReturn(adminClaims);

        String token = "adminToken";

        boolean result = bookingService.deleteBooking(bookingId, token);

        assertTrue(result);
        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBooking_UnauthorizedUser() {
        int bookingId = 1;
        Booking booking = new Booking();
        booking.setMember(new Member()); // Use a constructor that exists

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        String token = "invalidToken"; // Mock or create an invalid token

        assertThrows(UnauthorizedException.class, () -> bookingService.deleteBooking(bookingId, token));
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

    @Test
    void registerBooking_WithinAvailablePeriod_Success() {
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.of(2023, 10, 01, 10, 0)); // October 15, 2023, 10:00 AM
        form.setEndTime(LocalDateTime.of(2023, 10, 31, 12, 0));   // October 15, 2023, 12:00 PM

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
        doNothing().when(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        BookingDtoView result = bookingService.registerBooking(form);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
        verify(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        verify(bookingRepository).findOverlappingBookings(anyInt(), any(), any());
    }

    @Test
    void registerBooking_OutsideAvailablePeriod_ThrowsException() {
        BookingDtoForm form = new BookingDtoForm();
        form.setFacilityId(1);
        form.setMemberId(1);
        form.setStartTime(LocalDateTime.of(2023, 11, 15, 10, 0)); // November 15, 2023, 10:00 AM
        form.setEndTime(LocalDateTime.of(2023, 11, 15, 12, 0));   // November 15, 2023, 12:00 PM

        doThrow(new IllegalArgumentException("Facility not available"))
            .when(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());

        assertThrows(IllegalArgumentException.class, () -> bookingService.registerBooking(form));
        verify(facilityAvailabilityService).checkAvailabilityForBooking(anyInt(), any(), any());
        verify(bookingRepository, never()).save(any(Booking.class));
    }


}
