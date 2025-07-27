package com.example.locationTracker.feature;

import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tracker_phone")
    private UserEntity tracker; // User requesting/owning the feature

    private String name;

    @ManyToOne
    @JoinColumn(name = "trackee_phone")
    private UserEntity trackee; // User on which feature is implemented

    @Enumerated(EnumType.STRING)
    private FeatureStatus status = FeatureStatus.PENDING;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatureSchedule> schedules = new ArrayList<>();

    public abstract void onLocationUpdate(UserEntity trackee);

    public abstract String getFeatureType();

    public boolean isFeatureActiveNow() {
        if (schedules == null || schedules.isEmpty()) return true; // No schedule = always active

        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();
        LocalTime now = LocalTime.now();

        for (FeatureSchedule schedule : schedules) {
            boolean timeOk = !now.isBefore(schedule.getStartTime()) && !now.isAfter(schedule.getEndTime());
            boolean dateMatch = schedule.getActiveDates() != null && schedule.getActiveDates().contains(today);
            boolean dayMatch = schedule.getActiveDays() != null && schedule.getActiveDays().contains(day);

            if ((dayMatch || dateMatch) && timeOk) return true;
        }
        return false;
    }
}
