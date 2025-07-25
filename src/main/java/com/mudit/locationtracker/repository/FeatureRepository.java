package com.mudit.locationtracker.repository;


import com.mudit.locationtracker.Enums.FeatureStatus;
import com.mudit.locationtracker.feature.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    List<Feature> findByTrackeePhoneNumberAndStatus(String phoneNumber, FeatureStatus status);

}
