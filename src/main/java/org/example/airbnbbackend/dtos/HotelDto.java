package org.example.airbnbbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.example.airbnbbackend.models.HotelContactInfo;
import org.example.airbnbbackend.models.Room;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HotelDto {

    private Long id;
    private String name;
    private String city;
    private List<String> photos;
    private List<String> amenities;
    private HotelContactInfo hotelContactInfo;
    private Boolean isActive;
    private List<Room> rooms;
}
