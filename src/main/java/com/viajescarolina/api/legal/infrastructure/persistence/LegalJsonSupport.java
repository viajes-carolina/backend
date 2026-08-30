package com.viajescarolina.api.legal.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/** Helper de (de)serialización JSON compartido por las entidades Panache del bounded context legal. */
final class LegalJsonSupport {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private LegalJsonSupport() {}

    static <T> List<T> readList(String json, TypeReference<List<T>> type) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    static String writeList(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value != null ? value : List.of());
        } catch (Exception e) {
            return "[]";
        }
    }
}
