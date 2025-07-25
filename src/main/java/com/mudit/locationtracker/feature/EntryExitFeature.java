package com.mudit.locationtracker.feature;

import com.mudit.locationtracker.Enums.FeatureStatus;
import com.mudit.locationtracker.model.AreaEntity;
import com.mudit.locationtracker.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "entry_exit_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class EntryExitFeature extends Feature {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private AreaEntity area;

    private boolean inside = false;

    public EntryExitFeature(UserEntity tracker, UserEntity trackee, AreaEntity area) {
        super(null, tracker, trackee, FeatureStatus.PENDING);
        this.area = area;
    }

    @Override
    public String getFeatureType() {
        return "EntryExit";
    }

    @Override
    public void onLocationUpdate(UserEntity trackee) {
        if (getStatus() != FeatureStatus.APPROVED) return;
        if (trackee.getLocation() == null) return;

        double lat = Double.parseDouble(trackee.getLocation().getLatitude());
        double lon = Double.parseDouble(trackee.getLocation().getLongitude());

        // Log area details
        if (area != null && area.getCenterLocation() != null) {
            log.info("[Feature:{}] Area center=({}, {}), radius={} km",
                    getId(),
                    area.getCenterLocation().getLatitude(),
                    area.getCenterLocation().getLongitude(),
                    area.getRadiusInKm());
        } else {
            log.warn("[Feature:{}] Area or centerLocation is null!", getId());
        }

        boolean isInside = area.isInside(trackee.getLocation());

        log.info("[Feature:{}] Trackee={} current location=({}, {}), insideArea={} (previously={})",
                getId(), trackee.getPhoneNumber(), lat, lon, isInside, inside);

        if (isInside && !inside) {
            log.info("📍 {} ENTERED the area set by {}", trackee.getName(), getTracker().getName());
        } else if (!isInside && inside) {
            log.info("🚶 {} EXITED the area set by {}", trackee.getName(), getTracker().getName());
        }

        inside = isInside;
    }
}

