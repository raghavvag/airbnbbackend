package org.example.airbnbbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.example.airbnbbackend.models.Hotel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomDto {
    private Long id;

    private String type;
    private BigDecimal basePrice;
    private List<String> photos;
    private List<String> amenities;
    private Integer capacity;
    private Integer totalCount;
}
