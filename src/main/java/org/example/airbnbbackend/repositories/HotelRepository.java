package org.example.airbnbbackend.repositories;

import org.example.airbnbbackend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {

}
