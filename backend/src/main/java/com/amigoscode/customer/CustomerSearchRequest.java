package com.amigoscode.customer;

public record CustomerSearchRequest(
        String name,
        String email,
        Integer minAge,
        Integer maxAge,
        Gender gender
) {
} 