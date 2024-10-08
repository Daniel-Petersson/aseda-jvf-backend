package se.leiden.asedajvf.facilityTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.service.FacilityService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacilityServiceTest {

    @Mock
    private FacilityService facilityService;

    private Facility facility1;
    private Facility facility2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        facility1 = new Facility();
        facility1.setId(1L);
        facility1.setName("Facility 1");
        facility1.setDescription("Description 1");

        facility2 = new Facility();
        facility2.setId(2L);
        facility2.setName("Facility 2");
        facility2.setDescription("Description 2");
    }

    @Test
    void testGetAllFacilities() {
        List<Facility> expectedFacilities = Arrays.asList(facility1, facility2);
        when(facilityService.getAllFacilities()).thenReturn(expectedFacilities);

        List<Facility> actualFacilities = facilityService.getAllFacilities();

        assertEquals(expectedFacilities, actualFacilities);
        verify(facilityService, times(1)).getAllFacilities();
    }

    @Test
    void testGetFacilityById() throws ResourceNotFoundException {
        when(facilityService.getFacilityById(1)).thenReturn(facility1);

        Facility actualFacility = facilityService.getFacilityById(1);

        assertEquals(facility1, actualFacility);
        verify(facilityService, times(1)).getFacilityById(1);
    }

    @Test
    void testGetFacilityByIdNotFound() throws ResourceNotFoundException {
        when(facilityService.getFacilityById(3)).thenThrow(new ResourceNotFoundException("Facility not found"));

        assertThrows(ResourceNotFoundException.class, () -> facilityService.getFacilityById(3));
        verify(facilityService, times(1)).getFacilityById(3);
    }

    @Test
    void testGetFacilityByFacilityName() throws ResourceNotFoundException {
        when(facilityService.getFacilityByFacilityName("Facility 1")).thenReturn(facility1);

        Facility actualFacility = facilityService.getFacilityByFacilityName("Facility 1");

        assertEquals(facility1, actualFacility);
        verify(facilityService, times(1)).getFacilityByFacilityName("Facility 1");
    }

    @Test
    void testGetFacilityByFacilityNameNotFound() throws ResourceNotFoundException {
        when(facilityService.getFacilityByFacilityName("Non-existent Facility"))
            .thenThrow(new ResourceNotFoundException("Facility not found"));

        assertThrows(ResourceNotFoundException.class, () -> facilityService.getFacilityByFacilityName("Non-existent Facility"));
        verify(facilityService, times(1)).getFacilityByFacilityName("Non-existent Facility");
    }
}
