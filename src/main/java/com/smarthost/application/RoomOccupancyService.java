package com.smarthost.application;

import com.smarthost.model.AvailableRoomsCount;
import com.smarthost.model.GuestOfferRepository;
import com.smarthost.model.RoomsOccupancy;
import org.springframework.stereotype.Service;

@Service
class RoomOccupancyService {

    private final GuestOfferRepository guestOfferRepository;

    RoomOccupancyService(GuestOfferRepository guestOfferRepository) {
        this.guestOfferRepository = guestOfferRepository;
    }

    public RoomsOccupancy assignGuestOffersToAvailableRooms(AvailableRoomsCount availableRoomsCount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
