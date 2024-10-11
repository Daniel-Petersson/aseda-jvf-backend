package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.OpeningHoursDto;

import java.util.List;

public interface OpeningHoursService {
    List<OpeningHoursDto> getOpeningHoursByFacility(int facilityId);
    void createOpeningHours(OpeningHoursDto dto);
    void updateOpeningHours(int openingId, OpeningHoursDto dto);
    void deleteOpeningHours(int openingId);
}
