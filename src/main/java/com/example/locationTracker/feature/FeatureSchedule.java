package com.example.locationTracker.feature;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

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

    private LocalTime startTime;
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
}
