package com.kwawingu.payments.session.keys;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class MpesaSessionKey {

    private final String sessionKey;

    public MpesaSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public MpesaEncryptedSessionKey encrypt(MpesaPublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey pubKey = publicKey.toRsaPublicKey();
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encryptedBytes = rsaCipher.doFinal(sessionKey.getBytes());
        return new MpesaEncryptedSessionKey(Base64.getEncoder().encodeToString(encryptedBytes));
    }
}
