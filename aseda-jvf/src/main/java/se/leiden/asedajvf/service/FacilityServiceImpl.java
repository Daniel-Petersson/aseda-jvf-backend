package se.leiden.asedajvf.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.repository.FacilityRepository;
import java.util.List;
@AllArgsConstructor
@Service
public class FacilityServiceImpl implements FacilityService {


    private FacilityRepository facilityRepository;

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility getFacilityById(int facilityId)throws ResourceNotFoundException {
        return facilityRepository.findById(facilityId).orElseThrow(()-> new ResourceNotFoundException("Facility not found with id: " + facilityId));
    }

    @Override
    public Facility getFacilityByFacilityName(String facilityName) throws ResourceNotFoundException {
        return facilityRepository.findByNameIgnoreCase(facilityName).orElseThrow(()->new ResourceNotFoundException("Facility not found with name: " + facilityName));
    }
}