package org.example.airbnbbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airbnbbackend.dtos.RoomDto;
import org.example.airbnbbackend.services.RoomService;
import org.example.airbnbbackend.services.RoomServiceimpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asmin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomServiceimpl roomService;
    @PostMapping("/create/{HotelId}")
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long HotelId, @RequestBody RoomDto roomDto) {
        return roomService.createnewroom(HotelId, roomDto) != null ?
                ResponseEntity.ok(roomService.createnewroom(HotelId, roomDto)) :
                ResponseEntity.badRequest().build();

    }
    @GetMapping("/{roomid}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomid) {
        return ResponseEntity.ok(roomService.getroombyid(roomid));
    }
    @GetMapping
    public ResponseEntity<?> getRoomsByHotelId(@RequestParam Long HotelId) {
        return ResponseEntity.ok(roomService.getroomsbyhotelid(HotelId));
    }
    @DeleteMapping("/{roomid}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomid) {
        roomService.DeleteRoom(roomid);
        return ResponseEntity.noContent().build();
    }
}
