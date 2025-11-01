package org.example.airbnbbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.services.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto>createHotel(@RequestBody HotelDto hotelDto){
        HotelDto hotel = hotelService.createHotel(hotelDto);
        return new  ResponseEntity<>(hotel, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto>getHotelById(@PathVariable Long id){
        HotelDto hotelDto=hotelService.getHotelById(id);
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }

}
