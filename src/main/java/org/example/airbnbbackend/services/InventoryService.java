package org.example.airbnbbackend.services;

import org.example.airbnbbackend.models.Room;

public interface InventoryService {
    void initialiseroomforoneyear(Room room);
    void Deletefutureinventory(Room room);
}
