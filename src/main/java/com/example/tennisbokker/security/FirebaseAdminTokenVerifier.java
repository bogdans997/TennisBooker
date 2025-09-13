package com.example.tennisbokker.security;

import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebaseAdminTokenVerifier implements TokenVerifier {

    private final FirebaseAuth firebaseAuth;

    @Override
    public DecodedToken verify(String idToken) throws Exception {
        var decoded = firebaseAuth.verifyIdToken(idToken, true);
        return new DecodedToken(decoded.getUid(), decoded.getEmail(), decoded.getName());
    }
}
