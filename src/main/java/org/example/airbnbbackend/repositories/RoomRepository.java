package org.example.airbnbbackend.repositories;

import org.example.airbnbbackend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
