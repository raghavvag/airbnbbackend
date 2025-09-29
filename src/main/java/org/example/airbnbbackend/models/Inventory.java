package org.example.airbnbbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "inventories",
            uniqueConstraints = {

            @UniqueConstraint(  name="unique_room_hotel_date",columnNames = {"room_id","hotel_id", "date"})
})
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;
    @Column(nullable = false)
    private Integer totalCount;
    @Column(nullable = false,precision = 5,scale = 2)
    private BigDecimal surgeFactor;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private Boolean closed;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
