package com.example.locationTracker.feature;

import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tracker_phone")
    private UserEntity tracker; // User requesting/owning the feature

    @ManyToOne
    @JoinColumn(name = "trackee_phone")
    private UserEntity trackee; // User on which feature is implemented

    @Enumerated(EnumType.STRING)
    private FeatureStatus status = FeatureStatus.PENDING;

    // All features must implement their core logic
    public abstract void onLocationUpdate(UserEntity trackee);

    public abstract String getFeatureType();
}
