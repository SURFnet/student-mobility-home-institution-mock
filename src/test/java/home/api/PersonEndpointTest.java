package home.api;

import home.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

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
                .when()
                .formParam("access_token", accessToken)
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
                .get("/actuator/health")
                .then()
                .body("status", equalTo("UP"));
    }

}