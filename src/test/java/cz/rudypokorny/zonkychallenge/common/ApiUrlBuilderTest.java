package cz.rudypokorny.zonkychallenge.common;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiUrlBuilderTest {

    @Test
    void testBuildToStringWithParams() {
        String expectedUrl = "https://www.seznam.cz?ABC=value%20X&param1=calue";

        String actualUrl = ApiUrlBuilder.create("https://www.seznam.cz",
                Map.of("ABC", "value X", "param1", "calue"))
                .buildToString();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testBuildToStringWithParamsWithDate() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        String expectedDate = ZonedDateTime.now(fixedClock).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String expectedUrl = "https://www.seznam.cz?date=" + expectedDate;

        String actualUrl = ApiUrlBuilder.create("https://www.seznam.cz")
                .addDateQueryParam("date", ZonedDateTime.now(fixedClock))
                .buildToString();

        assertEquals(expectedUrl, actualUrl);
    }
}