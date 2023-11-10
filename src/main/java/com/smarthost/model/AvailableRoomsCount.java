package com.smarthost.model;

public record AvailableRoomsCount(
        int premium,
        int economy
) {
    public AvailableRoomsCount {
        if (premium < 0) {
            throw new IllegalArgumentException("premium must be greater or equal to 0");
        }
        if (economy < 0) {
            throw new IllegalArgumentException("economy must be greater or equal to 0");
        }
    }
}
