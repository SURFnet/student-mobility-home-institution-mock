package home.api;

import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OfferingEndpoint {

    //https://open-education-api.github.io/specification/v4/docs.html#tag/offerings
    @GetMapping(value = "/offerings/{offeringId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String offerings(@PathVariable("offeringId") String offeringId) throws IOException {
        return IOUtils.toString(new ClassPathResource("/data/offering.json").getInputStream());
    }
}
