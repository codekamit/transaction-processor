package com.orm.learn_orm.config;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


@Service
public class DailySequenceGenerator {

    private final StringRedisTemplate redisTemplate;

    private static final String ID_PREFIX = "AB";
    private static final String REDIS_KEY_PREFIX = "sequence:ab:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    public DailySequenceGenerator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Reserves a block of sequential IDs.
     * This is atomic and safe to call from multiple app instances.
     *
     * @param batchSize The number of IDs you need (e.g., 50).
     * @return The first ID in the reserved block (e.g., 1, 51, 101).
     */
    public long getNextIdBlock(int batchSize) {
        // 1. Get today's date and create the unique key for today.
        // e.g., "sequence:ab:251101" for Nov 1st, 2025
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        String redisKey = REDIS_KEY_PREFIX + datePart;

        // 2. Atomically increment the key by the batch size.
        // Redis `INCRBY` returns the *new* value (the end of the block).
        Long endOfBlock = redisTemplate.opsForValue().increment(redisKey, batchSize);

        if (endOfBlock == null) {
            throw new RuntimeException("Failed to get ID block from Redis.");
        }

        // 3. Set the key to expire. This handles the "daily reset" automatically.
        // We only need to run this once per day when the key is first created.
        // This is an efficient check that avoids calling EXPIRE on every request.
        if (endOfBlock == batchSize) {
            // Set expiry to 25 hours. This avoids any race conditions
            // with the key expiring at the exact stroke of midnight.
            redisTemplate.expire(redisKey, 25, TimeUnit.HOURS);
        }

        // 4. Calculate the *start* ID of the reserved block
        // Example:
        // - First call: batchSize=50, endOfBlock=50. Returns (50 - 50 + 1) = 1
        // - Second call: batchSize=50, endOfBlock=100. Returns (100 - 50 + 1) = 51
        return endOfBlock - batchSize + 1;
    }

    /**
     * Helper method to format the final ID string.
     *
     * @param datePart  The formatted date string (e.g., "251101").
     * @param numericId The sequential number (e.g., 51).
     * @return The fully formatted ID (e.g., "AB25110100000051").
     */
    public String formatId(String datePart, long numericId) {
        // Formats the number to 8 digits with leading zeros
        return ID_PREFIX + datePart + String.format("%08d", numericId);
    }
}
