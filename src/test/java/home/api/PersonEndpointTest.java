package home.api;

import home.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class PersonEndpointTest extends AbstractIntegrationTest {

    @Test
    void persons() throws Exception {
        given()
                .when()
                .auth().oauth2(opaqueAccessToken())
                .get("/persons/1")
                .then()
                .statusCode(SC_OK)
                .body("mail", equalTo("vandamme.mcw@universiteitvanharderwijk.nl"));
    }

}