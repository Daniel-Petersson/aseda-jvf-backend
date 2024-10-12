package se.leiden.asedajvf.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.OpeningHoursDto;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.OpeningHours;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.repository.OpeningHoursRepository;
import se.leiden.asedajvf.enums.Role;
import se.leiden.asedajvf.mapper.OpeningHoursDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OpeningHoursServiceImpl implements OpeningHoursService {
    private final OpeningHoursRepository repository;
    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;
    private final OpeningHoursDtoMapper mapper;


    @Override
    public List<OpeningHoursDto> getOpeningHoursByFacility(int facilityId) {
        return repository.findByFacilityId(facilityId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createOpeningHours(OpeningHoursDto dto) {
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID"));
        Member assignedLeader = memberRepository.findById(dto.getAssignedLeaderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (assignedLeader.getRole() != Role.ADMIN && assignedLeader.getRole() != Role.INSTRUCTOR) {
            throw new IllegalArgumentException("Assigned leader must be an ADMIN or INSTRUCTOR");
        }

        OpeningHours openingHours = mapper.toOpeningHours(dto, facility, assignedLeader);
        repository.save(openingHours);
    }

    @Override
    public void updateOpeningHours(int openingId, OpeningHoursDto dto) {
        OpeningHours openingHours = repository.findById(openingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid opening ID"));

        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID"));
        Member assignedLeader = memberRepository.findById(dto.getAssignedLeaderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (assignedLeader.getRole() != Role.ADMIN && assignedLeader.getRole() != Role.INSTRUCTOR) {
            throw new IllegalArgumentException("Assigned leader must be an ADMIN or INSTRUCTOR");
        }

        openingHours.setFacility(facility);
        openingHours.setOpeningTime(dto.getOpeningTime());
        openingHours.setClosingTime(dto.getClosingTime());
        openingHours.setAssignedLeader(assignedLeader);
        repository.save(openingHours);
    }

    @Override
    public void deleteOpeningHours(int openingId) {
        repository.deleteById(openingId);
    }

    @Override
    public List<OpeningHoursDto> getAllOpeningHours() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
