package org.example.airbnbbackend.services;

import org.example.airbnbbackend.dtos.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createnewroom(Long HotelId,RoomDto roomDto);
    List<RoomDto> getroomsbyotel(Long HotelId);
    RoomDto getroombyid(Long roomid);
    void DeleteRoom(Long roomid);

}
