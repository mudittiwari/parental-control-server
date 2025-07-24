package com.example.locationTracker.repository;

import com.example.locationTracker.feature.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    @Query("SELECT f FROM Feature f WHERE f.tracker.phoneNumber = :trackerPhone")
    List<Feature> findByTrackerPhoneNumber(String trackerPhone);

    @Query("SELECT f FROM Feature f WHERE f.trackee.phoneNumber = :trackeePhone AND f.status = :status")
    List<Feature> findByTrackeePhoneNumberAndStatus(String trackeePhone, com.example.locationTracker.feature.FeatureStatus status);
}