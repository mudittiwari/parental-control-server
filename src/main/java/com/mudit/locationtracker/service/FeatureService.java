package com.mudit.locationtracker.service;


import com.mudit.locationtracker.Enums.FeatureStatus;
import com.mudit.locationtracker.feature.Feature;
import com.mudit.locationtracker.dto.FeatureDTO;
import com.mudit.locationtracker.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final FeatureRepository featureRepository;

//    public List<Feature> getApprovedFeaturesForTrackee(String trackeePhone) {
//        return featureRepository.findByTrackeePhoneNumberAndStatus(trackeePhone, FeatureStatus.APPROVED);
//    }
    public List<FeatureDTO> getApprovedFeaturesForTrackee(String trackeePhone) {
        List<Feature> features = featureRepository.findByTrackeePhoneNumberAndStatus(trackeePhone, FeatureStatus.APPROVED);

        return features.stream().map(feature -> new FeatureDTO(
                feature.getId(),
                feature.getFeatureType(), // must be implemented in each Feature subclass
                feature.getTracker().getPhoneNumber(),
                feature.getTrackee().getPhoneNumber(),
                feature.getStatus().name()
        )).collect(Collectors.toList());
    }
}
