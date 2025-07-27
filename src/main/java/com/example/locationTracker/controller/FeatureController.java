package com.example.locationTracker.controller;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.dto.FeatureDTO;
import com.example.locationTracker.dto.FeatureScheduleDTO;
import com.example.locationTracker.dto.MutualFeatureRequest;
import com.example.locationTracker.feature.FeatureService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
@Slf4j
public class FeatureController {

    private final FeatureService featureService;

    /**
     * Request Entry-Exit Feature
     */
    @PostMapping("/request-entry-exit")
    public FeatureDTO requestEntryExit(@RequestBody FeatureRequest request) {
        AreaEntity area = buildArea(request);
        return featureService.toDTO(
                featureService.requestEntryExitFeature(
                        request.getName(),
                        request.getTrackerPhone(),
                        request.getTrackeePhone(),
                        area,
                        request.getSchedules()
                )
        );
    }

    /**
     * Request Entry Feature
     */
    @PostMapping("/request-entry")
    public FeatureDTO requestEntry(@RequestBody FeatureRequest request) {
        AreaEntity area = buildArea(request);
        return featureService.toDTO(
                featureService.requestEntryFeature(
                        request.getName(),
                        request.getTrackerPhone(),
                        request.getTrackeePhone(),
                        area,
                        request.getSchedules()
                )
        );
    }

    /**
     * Request Exit Feature
     */
    @PostMapping("/request-exit")
    public FeatureDTO requestExit(@RequestBody FeatureRequest request) {
        AreaEntity area = buildArea(request);
        return featureService.toDTO(
                featureService.requestExitFeature(
                        request.getName(),
                        request.getTrackerPhone(),
                        request.getTrackeePhone(),
                        area,
                        request.getSchedules()
                )
        );
    }

    /**
     * Approve Feature
     */
    @PostMapping("/{featureId}/approve")
    public FeatureDTO approveFeature(@PathVariable Long featureId,
                                     @RequestBody ApproveFeatureRequest request) {
        return featureService.toDTO(
                featureService.approveFeature(featureId, request.getTrackeePhone())
        );
    }

    @PostMapping("/{featureId}/reject")
    public FeatureDTO rejectFeature(@PathVariable Long featureId,
                                    @RequestBody ApproveFeatureRequest request) {
        return featureService.toDTO(
                featureService.rejectFeature(featureId, request.getTrackeePhone())
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

    /**
     * Get mutual features between two users.
     */
    @PostMapping("/between")
    public Map<String, List<FeatureDTO>> getMutualFeatures(@RequestBody MutualFeatureRequest request) {
        List<FeatureDTO> user1ToUser2 = featureService.getFeaturesBetween(request.getUser1(), request.getUser2());
        List<FeatureDTO> user2ToUser1 = featureService.getFeaturesBetween(request.getUser2(), request.getUser1());

        return Map.of(
                request.getUser1() + "_on_" + request.getUser2(), user1ToUser2,
                request.getUser2() + "_on_" + request.getUser1(), user2ToUser1
        );
    }

    /**
     * Utility method to build AreaEntity from request
     */
    private AreaEntity buildArea(FeatureRequest request) {
        return AreaEntity.builder()
                .centerLocation(new com.example.locationTracker.location.LocationEntity(
                        null,
                        String.valueOf(request.getLat()),
                        String.valueOf(request.getLon())
                ))
                .radiusInKm(request.getRadiusKm())
                .build();
    }

    // DTO used for all feature creation requests
    @Data
    public static class FeatureRequest {
        private String name;
        private String trackerPhone;
        private String trackeePhone;
        private double lat;
        private double lon;
        private double radiusKm;
        private List<FeatureScheduleDTO> schedules;
    }

    @Data
    public static class ApproveFeatureRequest {
        private String trackeePhone;
    }
}
