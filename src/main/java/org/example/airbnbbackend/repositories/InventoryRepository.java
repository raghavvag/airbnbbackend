package org.example.airbnbbackend.repositories;

import org.example.airbnbbackend.models.Inventory;
import org.example.airbnbbackend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    void deleteByRoomAndDateAfter(Room room, LocalDate today);
}
