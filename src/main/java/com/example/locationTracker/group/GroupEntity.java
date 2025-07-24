package com.example.locationTracker.group;

import com.example.locationTracker.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> members = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "group_admins",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<UserEntity> admins = new ArrayList<>();

    public void addMember(UserEntity user) {
        if (members == null) members = new ArrayList<>();
        if (!members.contains(user)) members.add(user);
    }

    public void addAdmin(UserEntity user) {
        if (admins == null) admins = new ArrayList<>();
        if (!admins.contains(user)) admins.add(user);
    }
}
