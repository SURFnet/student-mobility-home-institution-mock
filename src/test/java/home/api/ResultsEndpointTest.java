package home.api;

import home.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@ActiveProfiles(value = "test", inheritProfiles = true)
public class ResultsEndpointTest extends AbstractIntegrationTest {

    @Test
    void associations() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .post("/associations/me")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void associationsInvalidToken() throws IOException {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(false))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .post("/associations/me")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

}