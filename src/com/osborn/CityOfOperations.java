package com.osborn;

import java.time.ZoneId;

public enum CityOfOperations {
    AFRICA_LAGOS("Africa/Lagos"),
    AFRICA_CAIRO("Africa/Cairo"),
    AMERICA_MONTREAL("America/Toronto");

    private final String zone;

    CityOfOperations(String zone) {
        this.zone = zone;
    }

    public ZoneId getTimeZone(){
        return ZoneId.of(zone);
    }
}
