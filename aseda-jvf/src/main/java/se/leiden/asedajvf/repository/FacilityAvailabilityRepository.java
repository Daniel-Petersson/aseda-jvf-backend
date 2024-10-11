package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.FacilityAvailability;

import java.util.List;
@Repository
public interface FacilityAvailabilityRepository extends JpaRepository<FacilityAvailability, Integer> {
    List<FacilityAvailability> findByFacilityId(int facilityId);
}
