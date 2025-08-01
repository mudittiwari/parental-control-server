package com.example.locationTracker.dto;

import com.example.locationTracker.area.AreaEntity;
import com.example.locationTracker.feature.FeatureStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDTO {
    private Long id;
    private String name;
    private String trackerPhone;
    private String trackeePhone;
    private String type;
    private AreaEntity area;
    private FeatureStatus status;
    private List<FeatureScheduleDTO> schedules;
}
