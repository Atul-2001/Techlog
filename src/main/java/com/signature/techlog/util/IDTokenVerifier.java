package com.signature.techlog.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.signature.techlog.generator.UsernameGenerator;
import com.signature.techlog.model.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class IDTokenVerifier {

    private static final Logger LOGGER = LogManager.getLogger(IDTokenVerifier.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CLIENT_ID = "[YOUR CLIENT ID]";

    public static User verifyGoogleIdToken(String idTokenString) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();

            // (Receive idTokenString by HTTPS POST)

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                try {
                    GoogleIdToken.Payload payload = idToken.getPayload();

                    User user = new User();
                    user.setId(payload.getSubject());
                    user.setName((String) payload.get("name"));
                    user.setEmail(payload.getEmail());
                    user.setEmailVerified(payload.getEmailVerified());
                    user.setUsername(UsernameGenerator.generate(payload.getEmail()));
                    user.setProfile((String) payload.get("picture"));

                    return user;
                } catch (UsernameGenerator.InvalidEmailException ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage());
                    return null;
                }
            } else {
                LOGGER.log(Level.TRACE, "Invalid ID token. Token: " + idTokenString);
                return null;
            }
        } catch (IOException | GeneralSecurityException ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
            return null;
        }
    }
}