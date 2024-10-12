package se.leiden.asedajvf.facilityTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.dto.FacilityDto;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.service.FacilityServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFacilities() {
        Facility facility1 = new Facility();
        facility1.setId(1);
        facility1.setName("Facility 1");
        facility1.setDescription("Description 1");

        Facility facility2 = new Facility();
        facility2.setId(2);
        facility2.setName("Facility 2");
        facility2.setDescription("Description 2");
       List<Facility> expectedFacilities = Arrays.asList(facility1, facility2);

        when(facilityRepository.findAll()).thenReturn(expectedFacilities);

        List<FacilityDto> actualFacilities = facilityService.getAllFacilities();

        assertEquals(expectedFacilities, actualFacilities);
        verify(facilityRepository, times(1)).findAll();
    }

    @Test
    void testGetFacilityById_ExistingId() throws ResourceNotFoundException {
        int facilityId = 1;
        Facility expectedFacility = new Facility();
        expectedFacility.setId(facilityId);
        expectedFacility.setName("Facility 1");
        expectedFacility.setDescription("Description 1");

        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(expectedFacility));

        FacilityDto actualFacility = facilityService.getFacilityById(facilityId);

        assertEquals(expectedFacility, actualFacility);
        verify(facilityRepository, times(1)).findById(facilityId);
    }

    @Test
    void testGetFacilityById_NonExistingId() {
        int nonExistingId = 999;

        when(facilityRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facilityService.getFacilityById(nonExistingId));
        verify(facilityRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void testGetFacilityByFacilityName_ExistingName() throws ResourceNotFoundException {
        String facilityName = "Facility 1";
        Facility expectedFacility = new Facility();
        expectedFacility.setId(1);
        expectedFacility.setName(facilityName);
        expectedFacility.setDescription("Description 1");

        when(facilityRepository.findByNameIgnoreCase(facilityName)).thenReturn(Optional.of(expectedFacility));

        FacilityDto actualFacility = facilityService.getFacilityByFacilityName(facilityName);

        assertEquals(expectedFacility, actualFacility);
        verify(facilityRepository, times(1)).findByNameIgnoreCase(facilityName);
    }

    @Test
    void testGetFacilityByFacilityName_NonExistingName() {
        String nonExistingName = "Non-existing Facility";

        when(facilityRepository.findByNameIgnoreCase(nonExistingName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facilityService.getFacilityByFacilityName(nonExistingName));
        verify(facilityRepository, times(1)).findByNameIgnoreCase(nonExistingName);
    }
}
