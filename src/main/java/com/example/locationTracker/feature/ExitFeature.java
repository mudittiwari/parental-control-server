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
@Table(name = "exit_features")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ExitFeature extends Feature {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private AreaEntity area;

    private boolean triggered = false;

    public ExitFeature(String name, UserEntity tracker, UserEntity trackee, AreaEntity area, List<FeatureScheduleDTO> scheduleDTOs) {
        super(null, tracker, name, trackee, FeatureStatus.PENDING, null);
        this.area = area;
    }

    @Override
    public String getFeatureType() {
        return "Exit";
    }

    @Override
    public void onLocationUpdate(UserEntity trackee) {
        if (getStatus() != FeatureStatus.APPROVED || trackee.getLocation() == null) return;

        if (!isFeatureActiveNow()) {
            log.info("[Feature:{}] Skipped Exit check – Feature not active now based on schedule.", getId());
            return;
        }

        boolean isInside = area.isInside(trackee.getLocation());

        log.info("[Feature:{}] Trackee={} location={}, insideArea={}, previouslyTriggered={}",
                getId(),
                trackee.getPhoneNumber(),
                trackee.getLocation(),
                isInside,
                triggered
        );

        if (!isInside && !triggered) {
            log.info("🚶 {} EXITED the area set by {}", trackee.getName(), getTracker().getName());
            triggered = true;
        }
    }
}
