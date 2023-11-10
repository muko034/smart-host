package com.smarthost.model;

import java.math.BigDecimal;

public record RoomsOccupancy(Entry premium, Entry economy) {

    public record Entry(int count, BigDecimal sum) {
    }

}
