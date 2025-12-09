package org.example.airbnbbackend.services;

import lombok.RequiredArgsConstructor;
import org.example.airbnbbackend.dtos.RoomDto;
import org.example.airbnbbackend.exceptions.ResourceNotFounfException;
import org.example.airbnbbackend.models.Hotel;
import org.example.airbnbbackend.models.Room;
import org.example.airbnbbackend.repositories.HotelRepository;
import org.example.airbnbbackend.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceimpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    @Override
    public RoomDto createnewroom(Long HotelId,RoomDto roomDto) {
        Hotel hotel=hotelRepository.findById(HotelId).orElseThrow(()->new ResourceNotFounfException("Hotel not found with id "+HotelId));        Room room=modelMapper.map(roomDto,Room.class);
        Room room1=modelMapper.map(roomDto,Room.class);
        room1.setHotel(hotel);
       room1= roomRepository.save(room1);

        return modelMapper.map(room1,RoomDto.class);

    }

    @Override
    public List<RoomDto> getroomsbyotel(Long HotelId) {
        Hotel hotel=hotelRepository.findById(HotelId).orElseThrow(()->new ResourceNotFounfException("Hotel not found with id "+HotelId));
        return hotel.getRooms()
                .stream()
                .map((element)->modelMapper.map(element,RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getroombyid(Long roomid) {
        return null;
    }

    @Override
    public void DeleteRoom(Long roomid) {

    }
}
