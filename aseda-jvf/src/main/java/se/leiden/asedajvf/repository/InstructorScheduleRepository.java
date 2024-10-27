package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.leiden.asedajvf.model.InstructorSchedule;
import java.time.LocalDateTime;
import java.util.List;

public interface InstructorScheduleRepository extends JpaRepository<InstructorSchedule, Integer> {
    List<InstructorSchedule> findByInstructorId(Integer instructorId);
    List<InstructorSchedule> findByFacilityId(Integer facilityId);

    @Query("SELECT is FROM InstructorSchedule is WHERE is.instructor.id = :instructorId " +
           "AND is.facility.id = :facilityId " +
           "AND ((is.startTime <= :endTime AND is.endTime >= :startTime) " +
           "OR (is.startTime >= :startTime AND is.startTime < :endTime))")
    List<InstructorSchedule> findOverlappingSchedules(
        @Param("instructorId") Integer instructorId,
        @Param("facilityId") Integer facilityId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
