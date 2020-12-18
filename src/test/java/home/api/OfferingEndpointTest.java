package home.api;

import home.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class OfferingEndpointTest extends AbstractIntegrationTest {

    @Value("${security.user.name}")
    private String user;

    @Value("${security.user.password}")
    private String password;

    @Test
    void offeringsNoSecurity() {
        given()
                .when()
                .get("/nope/offerings/1")
                .then()
                .statusCode(SC_OK)
                .body("abbreviation", equalTo("Test-INFOMQNM-20FS"));
    }

    @Test
    void offeringsBasicAuth() {
        given()
                .when()
                .auth().preemptive().basic(user, password)
                .get("/basic/offerings/1")
                .then()
                .statusCode(SC_OK)
                .body("abbreviation", equalTo("Test-INFOMQNM-20FS"));
    }

    @Test
    void offeringsBasicAuthForbidden() {
        given()
                .when()
                .get("/basic/offerings/1")
                .then()
                .statusCode(SC_FORBIDDEN);

    }

    @Test
    void offeringsOauth2() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .get("/oauth2/offerings/1")
                .then()
                .statusCode(SC_OK)
                .body("abbreviation", equalTo("Test-INFOMQNM-20FS"));
    }

    @Test
    void offeringInvalidToken() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(false))
                .get("/oauth2/offerings/1")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }


    @Test
    void offeringsNoAuth() {
        given()
                .when()
                .get("/nope/offerings/1")
                .then()
                .statusCode(SC_OK)
                .body("abbreviation", equalTo("Test-INFOMQNM-20FS"));
    }

}