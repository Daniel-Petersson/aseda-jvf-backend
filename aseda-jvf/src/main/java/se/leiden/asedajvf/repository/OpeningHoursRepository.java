package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.OpeningHours;

import java.util.List;
@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Integer> {
    List<OpeningHours> findByFacilityId(int facilityId);
}
