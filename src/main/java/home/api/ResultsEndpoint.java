package home.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class ResultsEndpoint {

    private static final Log LOG = LogFactory.getLog(ResultsEndpoint.class);

    //https://open-education-api.github.io/specification/v4/docs.html#tag/associations
    @PostMapping(value = "/associations/me")
    public ResponseEntity<Void> results(BearerTokenAuthentication authentication,
                                  @RequestBody Map<String, Object> resultsMap) {
        Map<String, Object> tokenAttributes = authentication.getTokenAttributes();
        String eppn = (String) tokenAttributes.get("eduperson_principal_name");

        Map<String, Object> result = (Map<String, Object>) resultsMap.get("result");

        LOG.debug(String.format("Persons request for person %s with result %s", eppn, result.get("state")));

        return ResponseEntity.ok().build();
    }

}
