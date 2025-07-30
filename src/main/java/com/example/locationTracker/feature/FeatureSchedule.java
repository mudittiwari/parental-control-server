package com.example.locationTracker.feature;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "feature_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start time must not be null")
    private LocalTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(name = "feature_schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "day_of_week")
    private Set<DayOfWeek> activeDays;

    @ElementCollection
    @CollectionTable(name = "feature_schedule_dates", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "active_date")
    private List<LocalDate> activeDates;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @AssertTrue(message = "Either activeDays or activeDates must be provided")
    public boolean isValidScheduleWindow() {
        return (activeDays != null && !activeDays.isEmpty())
                || (activeDates != null && !activeDates.isEmpty());
    }
}
