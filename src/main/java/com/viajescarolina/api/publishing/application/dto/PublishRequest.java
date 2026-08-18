package com.viajescarolina.api.publishing.application.dto;

import java.util.List;

public record PublishRequest(
    String target, // 'ALL', 'HOME', 'PROMOTIONS', 'BLOG', 'ABOUT', 'CONTACT'
    List<String> customTags,
    String reason
) {}
