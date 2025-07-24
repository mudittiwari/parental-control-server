package com.example.locationTracker.feature;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.repository.FeatureRepository;
import com.example.locationTracker.repository.UserRepository;
import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;

    /**
     * Request creation of an Entry-Exit feature.
     * Initially saved with PENDING status (trackee must approve).
     */
    @Transactional
    public EntryExitFeature requestEntryExitFeature(String trackerPhone, String trackeePhone, AreaEntity area) {
        log.info("Requesting Entry-Exit Feature: tracker={}, trackee={}, area={}",
                trackerPhone, trackeePhone, area);

        UserEntity tracker = userRepository.findById(trackerPhone)
                .orElseThrow(() -> {
                    log.error("Tracker not found: {}", trackerPhone);
                    return new EntityNotFoundException("Tracker not found: " + trackerPhone);
                });

        UserEntity trackee = userRepository.findById(trackeePhone)
                .orElseThrow(() -> {
                    log.error("Trackee not found: {}", trackeePhone);
                    return new EntityNotFoundException("Trackee not found: " + trackeePhone);
                });

        EntryExitFeature feature = new EntryExitFeature(tracker, trackee, area);
        EntryExitFeature savedFeature = featureRepository.save(feature);

        log.info("Entry-Exit Feature created successfully: featureId={}, tracker={}, trackee={}",
                savedFeature.getId(), trackerPhone, trackeePhone);
        return savedFeature;
    }

    /**
     * Trackee approves a pending feature.
     */
    @Transactional
    public Feature approveFeature(Long featureId, String trackeePhone) {
        log.info("Approving feature: featureId={}, trackee={}", featureId, trackeePhone);

        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> {
                    log.error("Feature not found: {}", featureId);
                    return new EntityNotFoundException("Feature not found with ID: " + featureId);
                });

        if (!feature.getTrackee().getPhoneNumber().equals(trackeePhone)) {
            log.warn("Approval failed: Trackee mismatch (expected={}, got={})",
                    feature.getTrackee().getPhoneNumber(), trackeePhone);
            throw new IllegalArgumentException("Only the trackee can approve this feature.");
        }

        feature.setStatus(FeatureStatus.APPROVED);
        Feature updatedFeature = featureRepository.save(feature);

        log.info("Feature approved successfully: featureId={}", featureId);
        return updatedFeature;
    }

    /**
     * Get all pending requests for a trackee.
     */
    public List<Feature> getPendingRequests(String trackeePhone) {
        log.info("Fetching pending feature requests for trackee={}", trackeePhone);
        return featureRepository.findByTrackeePhoneNumberAndStatus(trackeePhone, FeatureStatus.PENDING);
    }

    /**
     * Get all approved features for a trackee (active features).
     */
    public List<Feature> getFeaturesForTrackee(String trackeePhone) {
        log.info("Fetching approved features for trackee={}", trackeePhone);
        return featureRepository.findByTrackeePhoneNumberAndStatus(trackeePhone, FeatureStatus.APPROVED);
    }

    public List<Feature> getFeaturesForTracker(String trackerPhone) {
        log.info("Fetching features for tracker={}", trackerPhone);
        return featureRepository.findByTrackerPhoneNumber(trackerPhone);
    }

    /**
     * Called when trackee updates location.
     * Triggers all features' onLocationUpdate logic for this trackee.
     */
    public void onTrackeeLocationUpdate(UserEntity trackee) {
        log.info("Trackee {} updated location: lat={}, lon={}",
                trackee.getPhoneNumber(),
                trackee.getLocation() != null ? trackee.getLocation().getLatitude() : "null",
                trackee.getLocation() != null ? trackee.getLocation().getLongitude() : "null");

        List<Feature> features = getFeaturesForTrackee(trackee.getPhoneNumber());
        log.info("Found {} active features for trackee={}", features.size(), trackee.getPhoneNumber());

        for (Feature feature : features) {
            log.debug("Checking featureId={} for trackee={}", feature.getId(), trackee.getPhoneNumber());
            feature.onLocationUpdate(trackee); // Polymorphic call
        }
    }

    // =============================
    //      FEATURE -> DTO MAPPER
    // =============================
    public FeatureDTO toDTO(Feature feature) {
        AreaEntity area = null;
        if (feature instanceof EntryExitFeature entryExitFeature) {
            area = entryExitFeature.getArea();
        }

        return FeatureDTO.builder()
                .id(feature.getId())
                .trackerPhone(feature.getTracker().getPhoneNumber())
                .trackeePhone(feature.getTrackee().getPhoneNumber())
                .status(feature.getStatus())
                .area(area)
                .build();
    }

    public List<FeatureDTO> toDTOList(List<Feature> features) {
        return features.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
