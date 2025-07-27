package com.example.locationTracker.repository;

import com.example.locationTracker.feature.Feature;
import com.example.locationTracker.feature.FeatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    @Query("SELECT f FROM Feature f WHERE f.tracker.phoneNumber = :trackerPhone")
    List<Feature> findByTrackerPhoneNumber(String trackerPhone);

    @Query("SELECT f FROM Feature f WHERE f.trackee.phoneNumber = :trackeePhone AND f.status = :status")
    List<Feature> findByTrackeePhoneNumberAndStatus(String trackeePhone, com.example.locationTracker.feature.FeatureStatus status);

    @Query("SELECT f FROM Feature f WHERE f.tracker.phoneNumber = :tracker AND f.trackee.phoneNumber = :trackee AND f.status = :status")
    List<Feature> findByTrackerPhoneNumberAndTrackeePhoneNumberAndStatus(
            String tracker,
            String trackee,
            FeatureStatus status
    );
}