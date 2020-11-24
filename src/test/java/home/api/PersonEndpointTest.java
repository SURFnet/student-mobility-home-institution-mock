package home.api;

import home.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

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
                .get("/persons/1")
                .then()
                .statusCode(SC_OK)
                .body("personId", equalTo("j.doe@example.com"))
                .body("mail", equalTo("j.doe@example.com"))
                .body("givenName", equalTo("John"))
                .body("surname", equalTo("Doe"));
    }

    @Test
    void personsInvalidToken() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(false))
                .get("/persons/1")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}