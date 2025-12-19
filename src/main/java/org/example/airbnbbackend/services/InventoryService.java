package org.example.airbnbbackend.services;

import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.dtos.SearchRequestDto;
import org.example.airbnbbackend.models.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initialiseroomforoneyear(Room room);
    void Deletefutureinventory(Room room);

    Page<HotelDto> searchhotels(SearchRequestDto searchRequestDto);
}
