package com.example.locationTracker.group;

import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_location_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupLocationShareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "user_phone")
    private UserEntity user;

    private boolean active = true;
}
