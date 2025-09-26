package org.example.airbnbbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @ElementCollection
    @CollectionTable(name = "hotel_photos", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "photo", columnDefinition = "TEXT")
    private List<String> photos;

    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity", columnDefinition = "TEXT")
    private List<String> amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Embedded
    private HotelContactInfo hotelContactInfo;
    @Column(nullable = false)
    private Boolean isActive;
}