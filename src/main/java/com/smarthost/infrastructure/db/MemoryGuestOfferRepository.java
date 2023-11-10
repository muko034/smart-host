package com.smarthost.infrastructure.db;

import com.smarthost.model.GuestOffer;
import com.smarthost.model.GuestOfferRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryGuestOfferRepository implements GuestOfferRepository {

    private final List<GuestOffer> storage = new ArrayList<>();


    @Override
    public void addGuestOffer(GuestOffer offer) {
        storage.add(offer);
    }

    @Override
    public List<GuestOffer> findAllGuestOffers() {
        return new ArrayList<>(storage);
    }

}
