package com.smarthost.infrastructure.api.rest;

import com.smarthost.application.RoomOccupancyService;
import com.smarthost.model.AvailableRoomsCount;
import com.smarthost.model.RoomsOccupancy;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms-occupancy")
@Validated
public class RoomOccupancyRestController {

    private final RoomOccupancyService roomOccupancyService;

    public RoomOccupancyRestController(RoomOccupancyService roomOccupancyService) {
        this.roomOccupancyService = roomOccupancyService;
    }

    @GetMapping
    public RoomsOccupancy calcRoomsOccupancy(
            @RequestParam @Min(0) Integer premium,
            @RequestParam @Min(0) Integer economy
    ) {
        return roomOccupancyService.calcRoomsOccupancy(new AvailableRoomsCount(premium, economy));
    }

}
