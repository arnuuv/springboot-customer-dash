package com.amigoscode.customer;

import java.util.Map;

public record CustomerAnalyticsDTO(
        Long totalCustomers,
        Map<String, Long> genderDistribution,
        Map<String, Long> ageDistribution,
        Long customersWithProfileImages,
        Double averageAge,
        Map<String, Long> recentRegistrations
) {
} 