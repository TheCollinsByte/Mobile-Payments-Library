package com.kwawingu.payments.session;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Environment;
import com.kwawingu.payments.Market;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.keys.MpesaEncryptedApiKey;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class MpesaSession  {

    private static final Logger LOG = LoggerFactory.getLogger(MpesaSession.class);

    private final ApiEndpoint apiEndpoint;
    private final SessionKeyGenerator sessionKeyGenerator;
    private final MpesaKeyProvider keyProvider;

    public MpesaSession(MpesaKeyProvider mpesaKeyProvider, Environment environment, Market market) {
        this.keyProvider = mpesaKeyProvider;
        this.apiEndpoint = new ApiEndpoint(environment, market);
        this.sessionKeyGenerator = new SessionKeyGenerator();
    }

    public MpesaEncryptedSessionKey getEncryptedSessionKey() throws SessionKeyUnavailableException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String contextUrl = apiEndpoint.getUrl(Service.GET_SESSION);
        MpesaEncryptedApiKey anEncryptedApiKey = null;
        try {
            anEncryptedApiKey = keyProvider.getApiKey().encrypt(keyProvider.getPublicKey());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }

        LOG.info("URL: {}", contextUrl);

        return sessionKeyGenerator.getSessionKeyOrThrow(anEncryptedApiKey, contextUrl).encrypt(keyProvider.getPublicKey());
    }

}
