package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.BookingDtoForm;
import se.leiden.asedajvf.dto.BookingDtoView;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;

@Component
public class BookingMapper {

    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;

    public BookingMapper(FacilityRepository facilityRepository, MemberRepository memberRepository) {
        this.facilityRepository = facilityRepository;
        this.memberRepository = memberRepository;
    }

    public BookingDtoView toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoView.builder()
                .title(booking.getTitle())
                .facilityId(booking.getFacility().getId())
                .memberId(booking.getMember().getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDtoForm bookingDtoForm) {
        Facility facility = facilityRepository.findById(bookingDtoForm.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID"));
        Member member = memberRepository.findById(bookingDtoForm.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        return Booking.builder()
                .title(bookingDtoForm.getTitle())
                .facility(facility)
                .member(member)
                .startTime(bookingDtoForm.getStartTime())
                .endTime(bookingDtoForm.getEndTime())
                .build();
    }
}
