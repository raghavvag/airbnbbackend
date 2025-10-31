package org.example.airbnbbackend.repositories;

import org.example.airbnbbackend.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
