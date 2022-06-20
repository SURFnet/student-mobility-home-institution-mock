package home.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.mail.MailBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
public class AssociationEndpoint {

    private static final Log LOG = LogFactory.getLog(AssociationEndpoint.class);

    private final MailBox mailBox;
    private final ObjectMapper objectMapper;

    public AssociationEndpoint(MailBox mailBox, ObjectMapper objectMapper) {
        this.mailBox = mailBox;
        this.objectMapper = objectMapper;
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/associations/paths/~1associations~1external~1me/post
    @PostMapping(value = "/associations/external/me")
    public ResponseEntity<Map<String, Object>> newAssociation(BearerTokenAuthentication authentication,
                                                  @RequestBody Map<String, Object> offeringAssociation) throws IOException, MessagingException {
        Map<String, Object> tokenAttributes = authentication.getTokenAttributes();
        String eppn = (String) tokenAttributes.get("eduperson_principal_name");
        String eduid = (String) tokenAttributes.get("eduid");
        String givenName = (String) tokenAttributes.get("given_name");
        String familyName = (String) tokenAttributes.get("family_name");
        String email = (String) tokenAttributes.get("email");

        LOG.debug(String.format("Associations PATCH request for person eppn %s and eduid %s with result %s", eppn, eduid, offeringAssociation));

        mailBox.sendNewAssociation(String.format("%s %s", givenName, familyName), email, objectMapper.writeValueAsString(offeringAssociation));

        Map<String, Object> map = objectMapper.readValue(new ClassPathResource("/data/association.json").getInputStream(), new TypeReference<Map<String, Object>>() {
        });
        map.put("associationId", UUID.randomUUID().toString());
        return ResponseEntity.ok(map);
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/associations/paths/~1associations~1{associationId}/patch
    @PatchMapping(value = "/associations/{associationId}")
    public ResponseEntity<Map<String, Object>> associationUpdate(BearerTokenAuthentication authentication,
                                                  @PathVariable("associationId") String associationId,
                                                  @RequestBody Map<String, Object> resultsMap) throws IOException, MessagingException {
        Map<String, Object> tokenAttributes = authentication.getTokenAttributes();
        String eppn = (String) tokenAttributes.get("eduperson_principal_name");
        String eduid = (String) tokenAttributes.get("eduid");
        String givenName = (String) tokenAttributes.get("given_name");
        String familyName = (String) tokenAttributes.get("family_name");
        String email = (String) tokenAttributes.get("email");

        LOG.debug(String.format("Associations PATCH request for person eppn %s and eduid %s with result %s", eppn, eduid, resultsMap));

        mailBox.sendUserAssociation(String.format("%s %s", givenName, familyName), email, objectMapper.writeValueAsString(resultsMap));

        Map<String, Object> map = objectMapper.readValue(new ClassPathResource("/data/association.json").getInputStream(), new TypeReference<Map<String, Object>>() {
        });
        map.put("associationId", associationId);
        return ResponseEntity.ok(map);
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
