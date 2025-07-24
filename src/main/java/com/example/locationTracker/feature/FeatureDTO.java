package com.example.locationTracker.feature;

import com.example.locationTracker.area.AreaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDTO {
    private Long id;
    private String trackerPhone;
    private String trackeePhone;
    private AreaEntity area;
    private FeatureStatus status;
}
