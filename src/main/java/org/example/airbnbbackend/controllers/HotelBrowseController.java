package org.example.airbnbbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.dtos.SearchRequestDto;
import org.example.airbnbbackend.repositories.InventoryRepository;
import org.example.airbnbbackend.services.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody SearchRequestDto searchRequestDto) {
          Page<HotelDto> page=inventoryService.searchhotels(searchRequestDto);
        return ResponseEntity.ok(page);
    }

}
