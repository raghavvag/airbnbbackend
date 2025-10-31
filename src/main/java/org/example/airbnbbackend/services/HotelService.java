package org.example.airbnbbackend.services;

import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.models.Hotel;

public interface HotelService {
    HotelDto createHotel(HotelDto hotel);
    HotelDto getHotelById(Long id);
}
