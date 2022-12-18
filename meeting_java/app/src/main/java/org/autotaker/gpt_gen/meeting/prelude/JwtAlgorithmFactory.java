package org.autotaker.gpt_gen.meeting.prelude;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.auth0.jwt.algorithms.Algorithm;

public class JwtAlgorithmFactory {
    public Algorithm create() {
        try {
            return Algorithm.ECDSA256(
                    (ECPublicKey) readPublicKeyFromFile("public.pem"),
                    (ECPrivateKey) readPrivateKeyFromFile("private.pem"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private PublicKey readPublicKeyFromFile(String filename) throws Exception {
        String publicKeyPEM = readKeyFromFile(filename);
        return getPublicKeyFromString(publicKeyPEM);
    }

    private PrivateKey readPrivateKeyFromFile(String filename) throws Exception {
        String privateKeyPEM = readKeyFromFile(filename);
        return getPrivateKeyFromString(privateKeyPEM);
    }

    private String readKeyFromFile(String filename) throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private PublicKey getPublicKeyFromString(String key) throws Exception {
        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----\n", "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.getMimeDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

    private PrivateKey getPrivateKeyFromString(String key) throws Exception {
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.getMimeDecoder().decode(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

}
