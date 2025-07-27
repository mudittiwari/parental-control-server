package com.example.locationTracker.feature;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.dto.FeatureScheduleDTO;
import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Table(name = "entry_features")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class EntryFeature extends Feature {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private AreaEntity area;

    private boolean triggered = false;

    public EntryFeature(String name, UserEntity tracker, UserEntity trackee, AreaEntity area, List<FeatureScheduleDTO> scheduleDTOs) {
        super(null, tracker, name, trackee, FeatureStatus.PENDING, null); // schedules will be set later
        this.area = area;
    }

    @Override
    public String getFeatureType() {
        return "Entry";
    }

    @Override
    public void onLocationUpdate(UserEntity trackee) {
        if (getStatus() != FeatureStatus.APPROVED || trackee.getLocation() == null) return;

        if (!isFeatureActiveNow()) {
            log.info("[Feature:{}] Skipped Entry check – Feature not active now based on schedule.", getId());
            return;
        }

        boolean isInside = area.isInside(trackee.getLocation());

        if (isInside && !triggered) {
            log.info("📍 {} ENTERED the area set by {}", trackee.getName(), getTracker().getName());
            triggered = true;
        }
    }
}

