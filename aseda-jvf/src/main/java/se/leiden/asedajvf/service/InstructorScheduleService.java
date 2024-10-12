package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.InstructorScheduleDto;

import java.util.List;

public interface InstructorScheduleService {
    InstructorScheduleDto createSchedule(InstructorScheduleDto dto);
    InstructorScheduleDto updateSchedule(Integer id, InstructorScheduleDto dto);
    boolean deleteSchedule(Integer id);
    InstructorScheduleDto getScheduleById(Integer id);
    List<InstructorScheduleDto> getSchedulesByInstructor(Integer instructorId);
    List<InstructorScheduleDto> getSchedulesByFacility(Integer facilityId);
    List<InstructorScheduleDto> getAllSchedules();
}
