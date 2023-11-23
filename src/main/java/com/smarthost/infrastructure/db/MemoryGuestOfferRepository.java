package com.smarthost.infrastructure.db;

import com.smarthost.model.GuestOffer;
import com.smarthost.model.GuestOfferRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

@Repository
public class MemoryGuestOfferRepository implements GuestOfferRepository {

    private final ConcurrentLinkedQueue<GuestOffer> storage = new ConcurrentLinkedQueue<>();

    @PostConstruct
    void init() {
        Stream.of("23", "45", "155", "374", "22", "99.99", "100", "101", "115", "209")
                .map(BigDecimal::new)
                .map(GuestOffer::new)
                .forEach(this::addGuestOffer);
    }


    @Override
    public void addGuestOffer(GuestOffer offer) {
        storage.add(offer);
    }

    @Override
    public List<GuestOffer> findAllGuestOffers() {
        return new ArrayList<>(storage);
    }

}
