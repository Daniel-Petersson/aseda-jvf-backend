package se.leiden.asedajvf.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.InstructorScheduleDto;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.mapper.InstructorScheduleMapper;
import se.leiden.asedajvf.model.InstructorSchedule;
import se.leiden.asedajvf.repository.InstructorScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstructorScheduleServiceImpl implements InstructorScheduleService {

    private final InstructorScheduleRepository instructorScheduleRepository;
    private final InstructorScheduleMapper instructorScheduleMapper;

    @Override
    public InstructorScheduleDto createSchedule(InstructorScheduleDto dto) {
        if (isOverlapping(dto)) {
            throw new IllegalArgumentException("The schedule overlaps with an existing schedule");
        }
        InstructorSchedule schedule = instructorScheduleMapper.toEntity(dto);
        InstructorSchedule savedSchedule = instructorScheduleRepository.save(schedule);
        return instructorScheduleMapper.toDto(savedSchedule);
    }

    private boolean isOverlapping(InstructorScheduleDto dto) {
        List<InstructorSchedule> overlappingSchedules = instructorScheduleRepository.findOverlappingSchedules(
            dto.getInstructorId(), dto.getFacilityId(), dto.getStartTime(), dto.getEndTime());
        return !overlappingSchedules.isEmpty();
    }

    @Override
    public InstructorScheduleDto updateSchedule(Integer id, InstructorScheduleDto dto) {
        InstructorSchedule existingSchedule = instructorScheduleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Schedule with id: " + id + " does not exist"));

        instructorScheduleMapper.updateEntityFromDto(dto, existingSchedule);
        InstructorSchedule updatedSchedule = instructorScheduleRepository.save(existingSchedule);
        return instructorScheduleMapper.toDto(updatedSchedule);
    }

    @Override
    public boolean deleteSchedule(Integer id) {
        if (!instructorScheduleRepository.existsById(id)) {
            return false;
        }
        instructorScheduleRepository.deleteById(id);
        return true;
    }

    @Override
    public InstructorScheduleDto getScheduleById(Integer id) {
        return instructorScheduleRepository.findById(id)
                .map(instructorScheduleMapper::toDto)
                .orElseThrow(() -> new DataNotFoundException("Schedule with id: " + id + " does not exist"));
    }

    @Override
    public List<InstructorScheduleDto> getSchedulesByInstructor(Integer instructorId) {
        return instructorScheduleRepository.findByInstructorId(instructorId).stream()
                .map(instructorScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorScheduleDto> getSchedulesByFacility(Integer facilityId) {
        return instructorScheduleRepository.findByFacilityId(facilityId).stream()
                .map(instructorScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorScheduleDto> getAllSchedules() {
        return instructorScheduleRepository.findAll().stream()
                .map(instructorScheduleMapper::toDto)
                .collect(Collectors.toList());
    }
}
