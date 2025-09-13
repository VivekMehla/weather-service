package com.example.weather.unit;

import com.example.weather.dto.DailyForecast;
import com.example.weather.integration.WeatherCache;
import com.example.weather.integration.impl.LFUCacheWithLRUTieBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LFUCacheWithLRUTieBreakerTest {

    private WeatherCache cache;

    @BeforeEach
    void setUp() {
        cache = new LFUCacheWithLRUTieBreaker(2);
    }

    @Test
    void shouldCacheAndRetrieve() {
        List<DailyForecast> forecast = List.of(new DailyForecast("now"));
        cache.put("city1", forecast);

        assertEquals(forecast, cache.get("city1"));
    }

    @Test
    void shouldEvictLFUCityWhenFull() {
        List<DailyForecast> f1 = List.of(new DailyForecast("c1"));
        List<DailyForecast> f2 = List.of(new DailyForecast("c2"));
        List<DailyForecast> f3 = List.of(new DailyForecast("c3"));

        cache.put("city1", f1); // freq 1
        cache.put("city2", f2); // freq 1
        cache.get("city1");     // freq city1 = 2

        cache.put("city3", f3); // should evict city2

        assertNotNull(cache.get("city1"));
        assertNull(cache.get("city2"));
        assertNotNull(cache.get("city3"));
    }

    @Test
    void shouldUseLRUWhenFrequenciesAreEqual() throws InterruptedException {
        cache.put("city1", List.of(new DailyForecast("c1")));
        Thread.sleep(10);
        cache.put("city2", List.of(new DailyForecast("c2")));

        cache.put("city3", List.of(new DailyForecast("c3"))); // Evict city1 (oldest)

        assertNull(cache.get("city1"));
        assertNotNull(cache.get("city2"));
        assertNotNull(cache.get("city3"));
    }
}
