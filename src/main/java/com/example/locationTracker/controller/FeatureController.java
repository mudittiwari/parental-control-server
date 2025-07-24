package com.example.locationTracker.controller;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.feature.FeatureDTO;
import com.example.locationTracker.feature.FeatureService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    /**
     * Request creation of an Entry-Exit feature.
     * Accepts JSON body and returns the created feature DTO.
     */
    @PostMapping("/request-entry-exit")
    public FeatureDTO requestEntryExit(@RequestBody EntryExitFeatureRequest request) {
        AreaEntity area = AreaEntity.builder()
                .centerLocation(new com.example.locationTracker.location.LocationEntity(
                        null, String.valueOf(request.getLat()), String.valueOf(request.getLon())))
                .radiusInKm(request.getRadiusKm())
                .build();

        return featureService.toDTO(
                featureService.requestEntryExitFeature(
                        request.getTrackerPhone(),
                        request.getTrackeePhone(),
                        area
                )
        );
    }

    /**
     * Trackee approves a pending feature.
     */
    @PostMapping("/{featureId}/approve")
    public FeatureDTO approveFeature(@PathVariable Long featureId,
                                     @RequestBody ApproveFeatureRequest request) {
        return featureService.toDTO(
                featureService.approveFeature(featureId, request.getTrackeePhone())
        );
    }

    /**
     * Get all pending feature requests for a trackee.
     */
    @GetMapping("/pending/{trackeePhone}")
    public List<FeatureDTO> getPending(@PathVariable String trackeePhone) {
        return featureService.getPendingRequests(trackeePhone)
                .stream()
                .map(featureService::toDTO)
                .toList();
    }

    /**
     * Get all active features for a tracker.
     */
    @GetMapping("/tracker/{trackerPhone}")
    public List<FeatureDTO> getFeaturesForTracker(@PathVariable String trackerPhone) {
        return featureService.getFeaturesForTracker(trackerPhone)
                .stream()
                .map(featureService::toDTO)
                .toList();
    }

    // DTO for Entry-Exit feature creation request
    @Data
    public static class EntryExitFeatureRequest {
        private String trackerPhone;
        private String trackeePhone;
        private double lat;
        private double lon;
        private double radiusKm;
    }

    // DTO for feature approval request
    @Data
    public static class ApproveFeatureRequest {
        private String trackeePhone;
    }
}
