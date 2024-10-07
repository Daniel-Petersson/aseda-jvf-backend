package se.leiden.asedajvf.repository;

import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.Booking;
import se.leiden.asedajvf.model.Member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<Booking> findByBookingId(long id);
    List<Booking> findAll();
    List<Booking> findByMemberId(Member memberId);
    // No need to declare save() and delete() methods as they're inherited from CrudRepository

    @Query("SELECT b FROM Booking b WHERE b.facilityId = :facilityId " +
           "AND ((b.startTime <= :endTime AND b.endTime >= :startTime) " +
           "OR (b.startTime >= :startTime AND b.startTime < :endTime))")
    List<Booking> findOverlappingBookings(@Param("facilityId") String facilityId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}
