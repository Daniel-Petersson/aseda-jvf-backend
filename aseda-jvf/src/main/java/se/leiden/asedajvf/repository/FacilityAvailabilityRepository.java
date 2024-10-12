package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.FacilityAvailability;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FacilityAvailabilityRepository extends JpaRepository<FacilityAvailability, Integer> {
    List<FacilityAvailability> findByFacilityId(int facilityId);
    
    List<FacilityAvailability> findByFacilityIdAndStartTimeBetween(int facilityId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT fa FROM FacilityAvailability fa WHERE fa.facility.id = :facilityId AND " +
           "((fa.startTime <= :endTime AND fa.endTime >= :startTime) OR " +
           "(fa.startTime >= :startTime AND fa.startTime < :endTime))")
    List<FacilityAvailability> findOverlappingAvailabilities(int facilityId, LocalDateTime startTime, LocalDateTime endTime);
}
