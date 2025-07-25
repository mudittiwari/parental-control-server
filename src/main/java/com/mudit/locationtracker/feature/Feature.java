package com.mudit.locationtracker.feature;


import com.mudit.locationtracker.Enums.FeatureStatus;
import com.mudit.locationtracker.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
