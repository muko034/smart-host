package com.smarthost.model;

import java.util.List;

public interface GuestOfferRepository {

    void addGuestOffer(GuestOffer offer);

    List<GuestOffer> findAllGuestOffers();

}
