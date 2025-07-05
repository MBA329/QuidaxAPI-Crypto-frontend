package com.codewithmosh.dryptoapi.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class VTPassUtilityServiceGateway implements UtilityServiceGateway {
    private static final ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");
    private static final DateTimeFormatter REQUEST_ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private String generateRequestId() {
        // Step 1: Get current datetime in Africa/Lagos
        LocalDateTime nowInLagos = LocalDateTime.now(LAGOS_ZONE);

        // Step 2: Format to "YYYYMMDDHHmm"
        String timePart = nowInLagos.format(REQUEST_ID_FORMAT);

        // Step 3: Append random alphanumeric string (e.g., from UUID)
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        // Final request_id
        return timePart + randomPart;
    }
}
