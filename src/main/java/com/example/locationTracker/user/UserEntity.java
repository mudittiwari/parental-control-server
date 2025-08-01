package com.example.locationTracker.user;

import com.example.locationTracker.feature.Feature;
import com.example.locationTracker.location.LocationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;   // Primary Key

    private String name;
    private String email;

    @Column(name = "p_key", length = 2048)
    private String pKey;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", unique = true)
    private LocationEntity location;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_phone"),
            inverseJoinColumns = @JoinColumn(name = "friend_phone")
    )
    private List<UserEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<Feature> features = new ArrayList<>();
}
