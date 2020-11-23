package home.api;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
public class PersonEndpoint {

    private static final Log LOG = LogFactory.getLog(PersonEndpoint.class);

    private final RestTemplate restTemplate = new RestTemplate();

    //https://open-education-api.github.io/specification/v4/docs.html#tag/persons/paths/~1persons~1{personId}/get
    @GetMapping(value = "/persons/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String persons(@PathVariable("personId") String personId, BearerTokenAuthentication authentication) throws IOException {
        LOG.info("Returning person endpoint for " + authentication.getName());
        return IOUtils.toString(new ClassPathResource("/data/person.json").getInputStream());
    }
}
