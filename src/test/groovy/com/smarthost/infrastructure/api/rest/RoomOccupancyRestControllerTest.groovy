package com.smarthost.infrastructure.api.rest

import com.smarthost.application.RoomOccupancyService
import com.smarthost.application.AvailableRoomsCount
import com.smarthost.application.RoomsOccupancy
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RoomOccupancyRestController)
class RoomOccupancyRestControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @SpringBean
    private RoomOccupancyService roomOccupancyService = Stub()

    def "should return status 200 and rooms occupancy in body"() {
        given:
        roomOccupancyService.calcRoomsOccupancy(_ as AvailableRoomsCount) >> aRoomsOccupancy()

        when:
        def result = mockMvc.perform(
                get("/api/rooms-occupancy")
                        .param("premium", "7")
                        .param("economy", "5")
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.premium.count").value(6))
                .andExpect(jsonPath("\$.premium.sum").value(1054))
                .andExpect(jsonPath("\$.economy.count").value(4))
                .andExpect(jsonPath("\$.economy.sum").value(189.99g))
    }

    def "should return status 400 when premium request parameter is missing"() {
        when:
        def result = mockMvc.perform(
                get("/api/rooms-occupancy")
                        .param("economy", "5")
        )

        then:
        result
                .andExpect(status().isBadRequest())
    }

    def "should return status 400 when economy request parameter is missing"() {
        when:
        def result = mockMvc.perform(
                get("/api/rooms-occupancy")
                        .param("premium", "7")
        )

        then:
        result
                .andExpect(status().isBadRequest())
    }

    def "should return status 400 when premium request parameter is negative"() {
        when:
        def result = mockMvc.perform(
                get("/api/rooms-occupancy")
                        .param("premium", "-1")
                        .param("economy", "5")
        )

        then:
        result
                .andExpect(status().isBadRequest())
    }

    def "should return status 400 when economy request parameter is negative"() {
        when:
        def result = mockMvc.perform(
                get("/api/rooms-occupancy")
                        .param("premium", "7")
                        .param("economy", "-1")
        )

        then:
        result
                .andExpect(status().isBadRequest())
    }

    private static RoomsOccupancy aRoomsOccupancy() {
        return new RoomsOccupancy(
                new RoomsOccupancy.Entry(6, new BigDecimal("1054")),
                new RoomsOccupancy.Entry(4, new BigDecimal("189.99")),
        )
    }
}
