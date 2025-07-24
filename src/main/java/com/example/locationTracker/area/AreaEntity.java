package com.example.locationTracker.area;

import com.example.locationTracker.location.LocationEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "center_location_id", unique = true)
    private LocationEntity centerLocation;

    private double radiusInKm;

    public boolean isInside(LocationEntity targetLocation) {
        double centerLat = Double.parseDouble(centerLocation.getLatitude());
        double centerLon = Double.parseDouble(centerLocation.getLongitude());
        double targetLat = Double.parseDouble(targetLocation.getLatitude());
        double targetLon = Double.parseDouble(targetLocation.getLongitude());

        double distance = haversine(centerLat, centerLon, targetLat, targetLon);
        System.out.println("[Area] Distance from center: " + distance + " km, radius=" + radiusInKm);

        return distance <= radiusInKm;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in KM
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
