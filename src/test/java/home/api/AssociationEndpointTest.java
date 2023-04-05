package home.api;

import home.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles(value = "test")
public class AssociationEndpointTest extends AbstractIntegrationTest {

    @Test
    void newAssociation() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .post("/associations/external/me")
                .then()
                .statusCode(SC_CREATED);
    }

    @Test
    void newAssociationsInvalidToken() throws IOException {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(false))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .post("/associations/external/me")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void patchAssociation() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .pathParam("associationId", "123456")
                .patch("/associations/{associationId}")
                .then()
                .statusCode(SC_OK)
                .body("associationId", equalTo("123456"));
    }

    @Test
    void associationsV4() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken(true))
                .body(Collections.singletonMap("result", "ok"))
                .contentType(ContentType.JSON)
                .post("/associations/me")
                .then()
                .statusCode(SC_OK);
    }

}