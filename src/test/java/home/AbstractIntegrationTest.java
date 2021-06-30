package home;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.security.oauth2.resourceserver.opaque-token.introspection-uri=http://localhost:8081/introspect"
})
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        RestAssured.port = port;
    }

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    protected String opaqueAccessToken(boolean valid) throws IOException {
        String file = String.format("data/%s.json", valid ? "introspect" : "introspect-invalid-token");
        return opaqueAccessToken(file);
    }

    protected String opaqueAccessToken(String fileName) throws IOException {
        String introspectResult = IOUtils.toString(new ClassPathResource(fileName).getInputStream());
        stubFor(post(urlPathMatching("/introspect")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(introspectResult)));
        return UUID.randomUUID().toString();
    }

}