package com.kwawingu.payments;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MpesaPaymentGateway implements MobilePayment {

    @Override
    public String SessionKey(String publicKey, String apiKey) {
        String getPublicKeyBase64 = System.getenv("MPESA_PUBLIC_KEY");
        String getApiKeyToEncrypt = System.getenv("MPESA_API_KEY");

        if (getPublicKeyBase64 == null || getApiKeyToEncrypt ==null) {
            System.err.println("Missing environment variables. Please set MPESA_PUBLIC_KEY and MPESA_API_KEY");
            System.exit(1);
        }

        byte[] publicKeyBytes = Base64.getDecoder().decode(getPublicKeyBase64);

        try {
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedBytes = rsaCipher.doFinal(getApiKeyToEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}