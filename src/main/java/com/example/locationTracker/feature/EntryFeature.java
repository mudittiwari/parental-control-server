package com.example.locationTracker.feature;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

    public EntryFeature(UserEntity tracker, UserEntity trackee, AreaEntity area) {
        super(null, tracker, trackee, FeatureStatus.PENDING);
        this.area = area;
    }

    @Override
    public String getFeatureType() {
        return "Entry";
    }

    @Override
    public void onLocationUpdate(UserEntity trackee) {
        if (getStatus() != FeatureStatus.APPROVED || trackee.getLocation() == null) return;

        boolean isInside = area.isInside(trackee.getLocation());

        if (isInside && !triggered) {
            log.info("📍 {} ENTERED the area set by {}", trackee.getName(), getTracker().getName());
            triggered = true;
        }
    }
}

