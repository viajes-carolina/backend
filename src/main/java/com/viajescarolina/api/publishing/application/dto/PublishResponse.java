package com.viajescarolina.api.publishing.application.dto;

import java.time.Instant;
import java.util.List;

public record PublishResponse(
    String status,
    List<String> revalidatedTags,
    Instant publishedAt,
    String triggeredBy,
    String message
) {}
