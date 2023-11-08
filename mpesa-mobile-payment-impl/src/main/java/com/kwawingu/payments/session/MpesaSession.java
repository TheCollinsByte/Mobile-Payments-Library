package com.kwawingu.payments.session;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.EncryptApiKey;
import com.kwawingu.payments.Environment;
import com.kwawingu.payments.SessionKeyGenerator;
import com.kwawingu.payments.Market;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MpesaSession  {

    private static final Logger LOG = LoggerFactory.getLogger(MpesaSession.class);

    private final ApiEndpoint apiEndpoint;
    private final SessionKeyGenerator sessionKeyGenerator;
    private final EncryptApiKey encryptApiKey;

    public MpesaSession(MpesaKeyProvider mpesaKeyProvider, Environment environment, Market market, Config config) {
        this.apiEndpoint = new ApiEndpoint(environment, market);
        this.sessionKeyGenerator = new SessionKeyGenerator();
        this.encryptApiKey = new EncryptApiKey(config);
    }

    public String getEncryptedSessionKey() throws SessionKeyUnavailableException {

        String contextUrl = apiEndpoint.getUrl(Service.GET_SESSION);
        String anEncryptedApiKey = encryptApiKey.generateAnEncryptApiKey();

        LOG.info("URL: {}", contextUrl);

        String generatedSessionKey = sessionKeyGenerator.getSessionKeyOrThrow(anEncryptedApiKey, contextUrl);
        return encryptApiKey.generateAnEncryptedSessionId(generatedSessionKey);
    }

}
