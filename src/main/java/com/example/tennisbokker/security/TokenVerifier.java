package com.example.tennisbokker.security;

public interface TokenVerifier {
    DecodedToken verify(String idToken) throws Exception;

    record DecodedToken(String uid, String email, String name) {}
}
