package com.viajescarolina.api.contact.domain;

public interface TurnstileVerificationService {
    boolean verifyToken(String token, String remoteIp);
}
