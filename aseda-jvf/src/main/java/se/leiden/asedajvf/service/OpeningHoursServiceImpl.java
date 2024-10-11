package se.leiden.asedajvf.service;

import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.OpeningHoursDto;
import se.leiden.asedajvf.mapper.OpeningHoursDtoMapper;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.OpeningHours;
import se.leiden.asedajvf.repository.FacilityRepository;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.repository.OpeningHoursRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpeningHoursServiceImpl implements OpeningHoursService {
    private final OpeningHoursRepository repository;
    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;
    private final OpeningHoursDtoMapper mapper;

    public OpeningHoursServiceImpl(OpeningHoursRepository repository, FacilityRepository facilityRepository, MemberRepository memberRepository, OpeningHoursDtoMapper mapper) {
        this.repository = repository;
        this.facilityRepository = facilityRepository;
        this.memberRepository = memberRepository;
        this.mapper = mapper;
    }

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

        openingHours.setFacility(facility);
        openingHours.setDayOfWeek(dto.getDayOfWeek());
        openingHours.setOpeningTime(LocalTime.parse(dto.getOpeningTime(), DateTimeFormatter.ISO_TIME));
        openingHours.setClosingTime(LocalTime.parse(dto.getClosingTime(), DateTimeFormatter.ISO_TIME));
        openingHours.setAssignedLeader(assignedLeader);
        repository.save(openingHours);
    }

    @Override
    public void deleteOpeningHours(int openingId) {
        repository.deleteById(openingId);
    }
}
