package se.leiden.asedajvf.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import se.leiden.asedajvf.dto.FacilityDto;
import se.leiden.asedajvf.exeptions.ResourceNotFoundException;
import se.leiden.asedajvf.mapper.FacilityMapper;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.repository.FacilityRepository;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FacilityServiceImpl implements FacilityService {


    private FacilityRepository facilityRepository;
    private FacilityMapper facilityMapper;

    @Override
    public List<FacilityDto> getAllFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        return facilities.stream().map(facilityMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public FacilityDto getFacilityById(int facilityId) throws ResourceNotFoundException {
        Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + facilityId));
        return facilityMapper.toDto(facility);
    }

    @Override
    public FacilityDto getFacilityByFacilityName(String facilityName) throws ResourceNotFoundException {
        Facility facility = facilityRepository.findByNameIgnoreCase(facilityName)
            .orElseThrow(() -> new ResourceNotFoundException("Facility not found with name: " + facilityName));
        return facilityMapper.toDto(facility);
    }

}