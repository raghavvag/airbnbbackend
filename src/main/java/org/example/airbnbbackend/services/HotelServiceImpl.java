package org.example.airbnbbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.exceptions.ResourceNotFounfException;
import org.example.airbnbbackend.models.Hotel;
import org.example.airbnbbackend.models.Room;
import org.example.airbnbbackend.repositories.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
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

    @Override
    public HotelDto updateHotelbyid(Long id, HotelDto hotelDto) {
        Optional<Hotel> hotel=hotelRepository.findById(hotelDto.getId());
        if(hotel.isPresent()){
            Hotel existingHotel=hotel.get();
            existingHotel.setName(hotelDto.getName());
            existingHotel.setCity(hotelDto.getCity());
            existingHotel.setPhotos(hotelDto.getPhotos());
            existingHotel.setAmenities(hotelDto.getAmenities());
            existingHotel.setHotelContactInfo(hotelDto.getHotelContactInfo());
            existingHotel.setIsActive(hotelDto.getIsActive());
            hotelRepository.save(existingHotel);
            return modelMapper.map(existingHotel, HotelDto.class);
        }
        else{
            throw new ResourceNotFounfException("Hotel not found with id "+id);
        }
    }

    @Override
    public void deleteHotelById(Long id) {
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFounfException("Hotel not found with id "+id));
        hotelRepository.delete(hotel);

    }

    @Override
    public void activateHotel(Long id) {
        Hotel hotel1=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFounfException("Hotel not found with id "+id));
        hotel1.setIsActive(true);
        hotelRepository.save(hotel1);
        for(Room room: hotel1.getRooms()){
            inventoryService.initialiseroomforoneyear(room);
        }
    }
}
