package home.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import home.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferingEndpointTest {//extends AbstractIntegrationTest {

    @Test
    void offerings() throws NoSuchProviderException, NoSuchAlgorithmException, JOSEException, JsonProcessingException {
        given()
                .when()
                .get("/offerings/1")
                .then()
                .statusCode(SC_OK)
                .body("abbreviation", equalTo("Test-INFOMQNM-20FS"));


    }

}