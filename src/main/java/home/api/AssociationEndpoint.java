package home.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.mail.MailBox;
import home.model.User;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@RestController
public class AssociationEndpoint extends AbstractDelayEndpoint{

    private static final Log LOG = LogFactory.getLog(AssociationEndpoint.class);

    private final MailBox mailBox;
    private final ObjectMapper objectMapper;
    private final String[] statuses = {"pending", "canceled", "denied", "associated"};
    private final Random random = new Random();


    public AssociationEndpoint(MailBox mailBox,
                               ObjectMapper objectMapper,
                               @Value("${delay.enabled}") boolean delayEnabled,
                               @Value("${delay.millis-association}") long delayMillis) {
        super(delayEnabled, delayMillis);
        this.mailBox = mailBox;
        this.objectMapper = objectMapper;
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/associations/paths/~1associations~1external~1me/post
    @PostMapping(value = "/associations/external/me")
    public ResponseEntity<Map<String, Object>> newAssociation(BearerTokenAuthentication authentication,
                                                              @RequestBody Map<String, Object> offeringAssociation) throws IOException, MessagingException {
        this.delayResponse();
        User user = new User(authentication.getTokenAttributes());

        LOG.debug(String.format("Associations PATCH request for person eppn %s and eduid %s with result %s",
                user.getEppn(), user.getEduid(), offeringAssociation));

        mailBox.sendNewAssociation(String.format("%s %s", user.getGivenName(), user.getFamilyName()),
                user.getEmail(), objectMapper.writeValueAsString(offeringAssociation));

        Map<String, Object> map = objectMapper.readValue(new ClassPathResource("/data/association.json").getInputStream(), new TypeReference<Map<String, Object>>() {
        });
        map.put("associationId", UUID.randomUUID().toString());

        addRandomState(map);

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/associations/paths/~1associations~1{associationId}/patch
    @PatchMapping(value = "/associations/{associationId}")
    public ResponseEntity<Map<String, Object>> associationUpdate(BearerTokenAuthentication authentication,
                                                                 @PathVariable("associationId") String associationId,
                                                                 @RequestBody Map<String, Object> resultsMap) throws IOException, MessagingException {
        this.delayResponse();
        User user = new User(authentication.getTokenAttributes());

        LOG.debug(String.format("Associations PATCH request for person eppn %s and eduid %s with result %s",
                user.getEppn(), user.getEduid(), resultsMap));

        mailBox.sendUserAssociation(String.format("%s %s", user.getGivenName(), user.getFamilyName()),
                user.getEmail(), objectMapper.writeValueAsString(resultsMap));

        Map<String, Object> map = objectMapper.readValue(new ClassPathResource("/data/association.json").getInputStream(), new TypeReference<Map<String, Object>>() {
        });
        map.put("associationId", associationId);

        addRandomState(map);

        return ResponseEntity.ok(map);
    }

    //https://open-education-api.github.io/specification/v4/docs.html#tag/associations
    @PostMapping(value = "/associations/me")
    public ResponseEntity<Map<String, Object>> results(BearerTokenAuthentication authentication,
                                                       @RequestBody Map<String, Object> resultsMap) throws JsonProcessingException, MessagingException {
        this.delayResponse();
        User user = new User(authentication.getTokenAttributes());

        LOG.debug(String.format("Associations POST request for person eppn %s and eduid %s with result %s",
                user.getEppn(), user.getEduid(), resultsMap));

        mailBox.sendUserResults(String.format("%s %s", user.getGivenName(), user.getFamilyName()),
                user.getEmail(), objectMapper.writeValueAsString(resultsMap));

        return ResponseEntity.ok(Collections.singletonMap("res", "ok"));
    }

    private void addRandomState(Map<String, Object> map) {
        String randomStatus = statuses[random.nextInt(statuses.length)];
        map.put("state",randomStatus);

        List<Map<String, String>> messages = new ArrayList<>();
        // Create a map for the message
        Map<String, String> messageDetails = new HashMap<>();
        messageDetails.put("language", "en-GB");
        messageDetails.put("value", "This is just a random state, since this is a demo only.");

        // Add message details to the list
        messages.add(messageDetails);
        map.put("message", messages);
    }

}
