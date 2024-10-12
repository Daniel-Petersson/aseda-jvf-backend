package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.InstructorScheduleDto;
import se.leiden.asedajvf.model.InstructorSchedule;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.Facility;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.repository.FacilityRepository;

@Component
public class InstructorScheduleMapper {

    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;

    public InstructorScheduleMapper(MemberRepository memberRepository, FacilityRepository facilityRepository) {
        this.memberRepository = memberRepository;
        this.facilityRepository = facilityRepository;
    }

    public InstructorScheduleDto toDto(InstructorSchedule instructorSchedule) {
        if (instructorSchedule == null) {
            return null;
        }
        return InstructorScheduleDto.builder()
                .id(instructorSchedule.getId())
                .instructorId(instructorSchedule.getInstructor().getId())
                .facilityId(instructorSchedule.getFacility().getId())
                .startTime(instructorSchedule.getStartTime())
                .endTime(instructorSchedule.getEndTime())
                .build();
    }

    public InstructorSchedule toEntity(InstructorScheduleDto dto) {
        if (dto == null) {
            return null;
        }
        Member instructor = memberRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor ID"));
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID"));

        InstructorSchedule instructorSchedule = new InstructorSchedule();
        instructorSchedule.setId(dto.getId());
        instructorSchedule.setInstructor(instructor);
        instructorSchedule.setFacility(facility);
        instructorSchedule.setStartTime(dto.getStartTime());
        instructorSchedule.setEndTime(dto.getEndTime());
        return instructorSchedule;
    }

    public void updateEntityFromDto(InstructorScheduleDto dto, InstructorSchedule instructorSchedule) {
        if (dto.getInstructorId() != null) {
            Member instructor = memberRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid instructor ID"));
            instructorSchedule.setInstructor(instructor);
        }
    }
}
