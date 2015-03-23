package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoClient;
import com.smartling.marketo.sdk.Email;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmailIntegrationTest {
    private static String identityEndpoint;
    private static String restEndpoint;
    private static String clientId;
    private static String clientSecret;

    private MarketoClient marketoClient;

    @BeforeClass
    public static void checkPreconditions() {
        identityEndpoint = System.getProperty("marketo.identity");
        restEndpoint = System.getProperty("marketo.rest");
        clientId = System.getProperty("marketo.clientId");
        clientSecret = System.getProperty("marketo.clientSecret");

        assertThat(identityEndpoint).overridingErrorMessage("Identity endpoint is missing").isNotEmpty();
        assertThat(restEndpoint).overridingErrorMessage("REST endpoint is missing").isNotEmpty();
        assertThat(clientId).overridingErrorMessage("Client ID is missing").isNotEmpty();
        assertThat(clientSecret).overridingErrorMessage("Client Secret is missing").isNotEmpty();
    }

    @Before
    public void setUp() throws Exception {
        marketoClient = MarketoClient.create(identityEndpoint, restEndpoint).withCredentials(clientId, clientSecret);
    }

    @Test
    public void shouldListEmails() throws Exception {
        List<Email> emails = marketoClient.listEmails(0, 1);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowAuthenticationError() throws Exception {
        MarketoClient invalid = MarketoClient.create(identityEndpoint, restEndpoint).withCredentials(clientId, "invalid");
        invalid.listEmails(0, 1);
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoClient.listEmails(20000, 5);
    }

    @Test
    public void shouldLoadEmail() throws Exception {
        Email email = marketoClient.loadEmailById(1004);

        assertThat(email).isNotNull();
        assertThat(email.getId()).isEqualTo(1004);
        assertThat(email.getName()).isNotEmpty();
    }

    @Test
    public void shouldReadEmailContent() throws Exception {
        List<EmailContentItem> contentItems = marketoClient.loadEmailContent(1004);

        assertThat(contentItems).isNotEmpty();
        assertThat(contentItems.get(0).getHtmlContent()).isNotNull();
        assertThat(contentItems.get(0).getTextContent()).isNotNull();
    }

    @Test
    public void shouldCloneEmail() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();

        Email clone = marketoClient.cloneEmail(1004, newEmailName, 14);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
    }

    @Test
    public void shouldCloneEmailViaShorthandMethod() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();
        Email email = marketoClient.loadEmailById(1004);

        Email clone = marketoClient.cloneEmail(email, newEmailName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
    }
}