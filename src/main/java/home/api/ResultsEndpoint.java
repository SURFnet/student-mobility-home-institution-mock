package home.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.mail.MailBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
public class ResultsEndpoint {

    private static final Log LOG = LogFactory.getLog(ResultsEndpoint.class);

    private final MailBox mailBox;
    private final ObjectMapper objectMapper;

    public ResultsEndpoint(MailBox mailBox, ObjectMapper objectMapper) {
        this.mailBox = mailBox;
        this.objectMapper = objectMapper;
    }

    //https://open-education-api.github.io/specification/v4/docs.html#tag/associations
    @PostMapping(value = "/associations/me")
    public ResponseEntity<Void> results(BearerTokenAuthentication authentication,
                                        @RequestBody Map<String, Object> resultsMap) throws JsonProcessingException, MessagingException {
        Map<String, Object> tokenAttributes = authentication.getTokenAttributes();
        String eppn = (String) tokenAttributes.get("eduperson_principal_name");
        String eduid = (String) tokenAttributes.get("eduid");
        String givenName = (String) tokenAttributes.get("given_name");
        String familyName = (String) tokenAttributes.get("family_name");
        String email = (String) tokenAttributes.get("email");

        LOG.debug(String.format("Associations POST request for person eppn %s and eduid %s with result %s", eppn, eduid, resultsMap));

        mailBox.sendUserResults(String.format("%s %s", givenName, familyName), email, objectMapper.writeValueAsString(resultsMap));

        return ResponseEntity.ok().build();
    }

}
