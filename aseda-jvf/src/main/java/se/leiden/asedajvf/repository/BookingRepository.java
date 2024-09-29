package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingId(long id);
    List<Booking> findAll();
    List<Booking> findByMemberId(Member memberId);
    void save(Booking booking);
    void delete(Booking booking);

}
