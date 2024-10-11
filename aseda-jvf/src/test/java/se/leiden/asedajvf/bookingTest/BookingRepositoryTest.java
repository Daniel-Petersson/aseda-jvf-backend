package se.leiden.asedajvf.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.BookingRepository;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Facility facility;
    private Member member;

    @BeforeEach
    public void setUp() {
        bookingRepository.deleteAll();
        facilityRepository.deleteAll();
        memberRepository.deleteAll();

        facility = new Facility();
        facility.setName("Test Facility");
        facility.setDescription("Test Facility Description");
        facilityRepository.save(facility);

        member = new Member();
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setEmail("john.doe@example.com");
        member.setAddress("123 Main St");
        member.setCity("Test City");
        member.setPostalCode(12345); // Ensure this is a valid postal code
        member.setPassword("securePassword");
        memberRepository.save(member);
    }

    @Test
    public void testFindById() {
        Booking booking = new Booking();
        booking.setTitle("Test Booking"); // Set the title
        booking.setFacility(facility);
        booking.setMember(member);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        Booking savedBooking = bookingRepository.save(booking);

        Optional<Booking> foundBooking = bookingRepository.findById(savedBooking.getId());
        assertThat(foundBooking).isPresent();

        // Initialize the facility to avoid LazyInitializationException
        Facility foundFacility = foundBooking.get().getFacility();
        assertThat(foundFacility.getName()).isEqualTo(facility.getName());
        assertThat(foundFacility.getDescription()).isEqualTo(facility.getDescription());
    }

    @Test
    public void testFindByMemberId() {
        Booking booking = new Booking();
        booking.setTitle("Test Booking"); // Set the title
        booking.setFacility(facility);
        booking.setMember(member);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByMemberId(member.getId()); // Pass member ID
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.get(0).getMember()).isEqualTo(member);
    }

    @Test
    public void testFindOverlappingBookings() {
        Booking booking = new Booking();
        booking.setTitle("Test Booking"); // Set the title
        booking.setFacility(facility);
        booking.setMember(member);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                facility.getId(),
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().plusMinutes(30)
        );

        assertThat(overlappingBookings).isNotEmpty();
        assertThat(overlappingBookings.get(0).getFacility()).isEqualTo(facility);
    }
}
