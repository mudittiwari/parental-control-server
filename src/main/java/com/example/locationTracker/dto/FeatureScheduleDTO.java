package com.example.locationTracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class FeatureScheduleDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private Set<DayOfWeek> activeDays;
    private List<LocalDate> activeDates;
}
