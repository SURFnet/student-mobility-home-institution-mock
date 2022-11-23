package home.api;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class OfferingEndpoint extends AbstractDelayEndpoint {

    private static final Log LOG = LogFactory.getLog(OfferingEndpoint.class);

    public OfferingEndpoint(@Value("${delay.enabled}") boolean delayEnabled,
                            @Value("${delay.millis-offering}") long delayMillis) {
        super(delayEnabled, delayMillis);
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/offerings/paths/~1offerings~1{offeringId}/get
    @GetMapping(value = {"oauth2/offerings/{offeringId}", "basic/offerings/{offeringId}", "none/offerings/{offeringId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String offerings(HttpServletRequest request, @PathVariable("offeringId") String offeringId) throws IOException {
        this.delayResponse();

        String requestURI = request.getRequestURI();

        LOG.debug(String.format("Offering request for URI %s", requestURI));

        return IOUtils.toString(new ClassPathResource("/data/offering_program_v5.json").getInputStream());
    }
}
