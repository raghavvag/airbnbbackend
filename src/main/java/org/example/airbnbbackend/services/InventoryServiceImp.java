package org.example.airbnbbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.airbnbbackend.dtos.HotelDto;
import org.example.airbnbbackend.dtos.SearchRequestDto;
import org.example.airbnbbackend.models.Inventory;
import org.example.airbnbbackend.models.Room;
import org.example.airbnbbackend.repositories.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImp implements InventoryService {

    private  final InventoryRepository inventoryRepository;
    @Override
    public void initialiseroomforoneyear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusYears(1);
        for (LocalDate date = today; date.isBefore(end); date = date.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .room(room)
                    .hotel(room.getHotel())
                    .date(date)
                    .bookedCount(0)
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(room.getBasePrice())
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            log.info("Initialized inventory for room {} on date {}", room.getId(), date);
        }

    }

    @Override
    public void Deletefutureinventory(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoomAndDateAfter(room, today);
    }

    @Override
    public Page<HotelDto> searchhotels(SearchRequestDto searchRequestDto) {
        pageable = PageRequest.of(searchRequestDto.getPageNumber(), searchRequestDto.getPageSize());
    }
}
