package se.leiden.asedajvf.facilityTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.repository.FacilityRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
public class FacilityRepositoryTest {

    @Autowired
    private FacilityRepository facilityRepository;

    @Test
    public void testFindByNameIgnoreCase() {
        Facility facility = new Facility();
        facility.setName("Test Facility");
        facility.setDescription("Test Description");
        facilityRepository.save(facility);

        Optional<Facility> foundFacility = facilityRepository.findByNameIgnoreCase("test facility");
        assertThat(foundFacility).isPresent();
        assertThat(foundFacility.get().getName()).isEqualTo("Test Facility");
    }

    @Test
    public void testFindById() {
        Facility facility = new Facility();
        facility.setName("Another Facility");
        facility.setDescription("Another Description");
        Facility savedFacility = facilityRepository.save(facility);

        Optional<Facility> foundFacility = facilityRepository.findById(savedFacility.getId());
        assertThat(foundFacility).isPresent();
        assertThat(foundFacility.get().getName()).isEqualTo("Another Facility");
    }

    @Test
    public void testSaveFacility() {
        Facility facility = new Facility();
        facility.setName("New Facility");
        facility.setDescription("New Description");
        Facility savedFacility = facilityRepository.save(facility);

        assertThat(savedFacility.getId()).isNotNull();
        assertThat(savedFacility.getName()).isEqualTo("New Facility");
    }
}
