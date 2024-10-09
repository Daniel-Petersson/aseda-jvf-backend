package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.Booking;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

import org.springframework.data.repository.query.Param;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findById(int id);
    List<Booking> findAll();
    List<Booking> findByMemberId(Integer memberId);
    // No need to declare save() and delete() methods as they're inherited from CrudRepository

    @Query("SELECT b FROM Booking b WHERE b.facility.id = :facilityId AND ((b.startTime <= :endTime AND b.endTime >= :startTime) OR (b.startTime >= :startTime AND b.startTime < :endTime))")
    List<Booking> findOverlappingBookings(@Param("facilityId") Integer facilityId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}
