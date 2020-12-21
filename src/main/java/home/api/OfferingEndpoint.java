package home.api;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class OfferingEndpoint {

    private static final Log LOG = LogFactory.getLog(OfferingEndpoint.class);

    //https://open-education-api.github.io/specification/v4/docs.html#tag/offerings
    @GetMapping(value = {"oauth2/offerings/{offeringId}", "basic/offerings/{offeringId}", "none/offerings/{offeringId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String offerings(HttpServletRequest request, @PathVariable("offeringId") String offeringId) throws IOException {
        String requestURI = request.getRequestURI();

        LOG.debug(String.format("Offering request for URI %s", requestURI));

        return IOUtils.toString(new ClassPathResource("/data/offering.json").getInputStream());
    }
}
