package com.smarthost.application;

import com.smarthost.model.GuestOffer;
import com.smarthost.model.GuestOfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class RoomOccupancyService {

    private static final BigDecimal MIN_PREMIUM_ROOM_PRICE = new BigDecimal(100);

    private final GuestOfferRepository guestOfferRepository;

    RoomOccupancyService(GuestOfferRepository guestOfferRepository) {
        this.guestOfferRepository = guestOfferRepository;
    }

    public RoomsOccupancy calcRoomsOccupancy(AvailableRoomsCount availableRoomsCount) {
        final var guestOffersByCategory = guestOfferRepository.findAllGuestOffers().stream()
                .sorted(Comparator.comparing(GuestOffer::amount).reversed())
                .collect(Collectors.partitioningBy(this::isOfferPremiumPriced));
        final var takenPremiumOffers = guestOffersByCategory.get(true).stream().limit(availableRoomsCount.premium())
                .collect(Collectors.toCollection(ArrayList::new));
        final var economyOffers = guestOffersByCategory.get(false);
        final var availablePremiumRoomsCount = availableRoomsCount.premium() - takenPremiumOffers.size();
        if (availablePremiumRoomsCount > 0
                && isMoreEconomyOffersThanRoomsAvailable(availableRoomsCount, economyOffers.size())) {
            final var upgradedOffers = economyOffers.stream().limit(availablePremiumRoomsCount).toList();
            takenPremiumOffers.addAll(upgradedOffers);
            economyOffers.removeAll(upgradedOffers);
        }
        final var takenEconomyOffers = economyOffers.stream().limit(availableRoomsCount.economy()).toList();
        return new RoomsOccupancy(aggregate(takenPremiumOffers), aggregate(takenEconomyOffers));
    }

    private static boolean isMoreEconomyOffersThanRoomsAvailable(
            AvailableRoomsCount availableRoomsCount,
            int economyOffersCount) {
        return economyOffersCount > availableRoomsCount.economy();
    }

    private boolean isOfferPremiumPriced(GuestOffer offer) {
        return offer.amount().compareTo(MIN_PREMIUM_ROOM_PRICE) >= 0;
    }

    private RoomsOccupancy.Entry aggregate(Collection<GuestOffer> offers) {
        return new RoomsOccupancy.Entry(
                offers.size(),
                offers.stream().map(GuestOffer::amount).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

}
