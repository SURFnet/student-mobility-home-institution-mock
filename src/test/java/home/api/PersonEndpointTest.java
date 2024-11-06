package home.api;

import home.AbstractIntegrationTest;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class PersonEndpointTest extends AbstractIntegrationTest {

    @Test
    void persons() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .get("/persons/me")
                .then()
                .statusCode(SC_OK)
                .body("personId", equalTo("j.doe@example.com"))
                .body("mail", equalTo("j.doe@example.com"))
                .body("givenName", equalTo("John"))
                .body("surname", equalTo("Doe"))
                .body("displayName", equalTo("John Doe"));
    }

    @Test
    void personsPost() throws Exception {
        String accessToken = opaqueAccessToken(true);
        given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                ///Bugfix for the DefaultBearerTokenResolver being strict in content type checking
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .when()
                .formParam("access_token", accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .post("/persons/me")
                .then()
                .statusCode(SC_OK)
                .body("personId", equalTo("j.doe@example.com"))
                .body("mail", equalTo("j.doe@example.com"))
                .body("givenName", equalTo("John"))
                .body("surname", equalTo("Doe"))
                .body("displayName", equalTo("John Doe"));
    }

    @Test
    void personsMinimal() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken("data/introspect-minimal.json"))
                .get("/persons/me")
                .then()
                .statusCode(SC_OK)
                .body("personId", equalTo("j.doe@example.com"))
                .body("mail", equalTo("vandamme.mcw@universiteitvanharderwijk.nl"))
                .body("givenName", equalTo("Maartje"))
                .body("surname", equalTo("Damme"))
                .body("displayName", equalTo("Maartje van Damme"));
    }

    @Test
    void personsInvalidToken() throws IOException {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(false))
                .get("/persons/me")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void status() {
        given()
                .when()
                .get("/internal/health")
                .then()
                .body("status", equalTo("UP"));
    }

}