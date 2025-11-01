package org.example.airbnbbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.exceptions.ResourceNotFounfException;
import org.example.airbnbbackend.models.Hotel;
import org.example.airbnbbackend.repositories.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("creating hotel with hotel name",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto, Hotel.class);
        hotel.setIsActive(false);
        hotel=hotelRepository.save(hotel);
        log.info("created hotel with hotel name",hotelDto.getName());
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("geting hotel with id",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFounfException("Hotel not found with id "+id));
        return modelMapper.map(hotel, HotelDto.class);

    }
}
