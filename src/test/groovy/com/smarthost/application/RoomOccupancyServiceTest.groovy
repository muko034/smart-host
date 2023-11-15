package com.smarthost.application

import com.smarthost.infrastructure.db.MemoryGuestOfferRepository
import com.smarthost.model.GuestOffer
import com.smarthost.model.GuestOfferRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RoomOccupancyServiceTest extends Specification {

    @Subject
    private RoomOccupancyService roomOccupancyService = new RoomOccupancyService(aGuestOfferRepository())

    @Unroll
    def 'should calculate rooms occupancy properly'() {
        given:
        def input = new AvailableRoomsCount(availablePremiumRommsCount, availableEconomyRommsCount)

        when:
        def result = roomOccupancyService.calcRoomsOccupancy(input)

        then:
        result.premium().count() == expectedPremiumRoomsCount
        result.premium().sum() == expectedPremiumRoomsSum
        result.economy().count() == expectedEconomyRoomsCount
        result.economy().sum() == expectedEconomyRoomsSum

        where:
        availablePremiumRommsCount | availableEconomyRommsCount || expectedPremiumRoomsCount | expectedPremiumRoomsSum | expectedEconomyRoomsCount | expectedEconomyRoomsSum
        3                          | 3                          || 3                         | 738.0g                  | 3                         | 167.99g
        7                          | 5                          || 6                         | 1054.0g                 | 4                         | 189.99g
        2                          | 7                          || 2                         | 583.0g                  | 4                         | 189.99g
        7                          | 1                          || 7                         | 1153.99g                | 1                         | 45.0g // error in task description
    }

    private static GuestOfferRepository aGuestOfferRepository() {
        def repository = new MemoryGuestOfferRepository()
        [23.0g, 45.0g, 155.0g, 374.0g, 22.0g, 99.99g, 100.0g, 101.0g, 115.0g, 209.0g].forEach {
            repository.addGuestOffer(new GuestOffer(it))
        }
        return repository
    }

}
