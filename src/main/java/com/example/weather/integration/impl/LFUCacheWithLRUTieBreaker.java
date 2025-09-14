package com.example.weather.integration.impl;

import com.example.weather.dto.DailyForecast;
import com.example.weather.integration.WeatherCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LFUCacheWithLRUTieBreaker implements WeatherCache {

    private static final Logger logger = LoggerFactory.getLogger(LFUCacheWithLRUTieBreaker.class);

    private final int maxSize;
    private final Map<String, List<DailyForecast>> cache = new HashMap<>();
    private final Map<String, Integer> frequencyMap = new HashMap<>();
    private final Map<String, Long> lastAccessMap = new HashMap<>();

    public LFUCacheWithLRUTieBreaker(int maxSize) {
        this.maxSize = maxSize;
        logger.info("Initialized LFUCacheWithLRUTieBreaker with max size: {}", maxSize);
    }

    @Override
    public synchronized List<DailyForecast> get(String city) {
        if (!cache.containsKey(city)) {
            logger.debug("Cache miss for city: {}", city);
            return null;
        }

        frequencyMap.put(city, frequencyMap.getOrDefault(city, 0) + 1);
        lastAccessMap.put(city, System.nanoTime());
        logger.debug("Cache hit for city: {} (freq: {})", city, frequencyMap.get(city));
        return cache.get(city);
    }

    @Override
    public synchronized void put(String city, List<DailyForecast> forecast) {
        if (cache.size() >= maxSize && !cache.containsKey(city)) {
            logger.info("Cache size exceeded. Evicting least frequently used entry.");
            evict();
        }

        cache.put(city, forecast);
        frequencyMap.put(city, frequencyMap.getOrDefault(city, 0) + 1);
        lastAccessMap.put(city, System.nanoTime());
        logger.debug("Inserted/Updated forecast for city: {} (current size: {})", city, cache.size());
    }

    private void evict() {
        String lfuCity = null;
        int minFreq = Integer.MAX_VALUE;
        long oldestAccess = Long.MAX_VALUE;

        for (String key : cache.keySet()) {
            int freq = frequencyMap.getOrDefault(key, 0);
            long access = lastAccessMap.getOrDefault(key, 0L);

            if (freq < minFreq || (freq == minFreq && access < oldestAccess)) {
                minFreq = freq;
                oldestAccess = access;
                lfuCity = key;
            }
        }

        if (lfuCity != null) {
            cache.remove(lfuCity);
            frequencyMap.remove(lfuCity);
            lastAccessMap.remove(lfuCity);
            logger.info("Evicted city: {} (freq: {}, last access: {})", lfuCity, minFreq, oldestAccess);
        }
    }
}
