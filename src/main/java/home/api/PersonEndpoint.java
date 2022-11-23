package home.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.webresources.AbstractArchiveResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class PersonEndpoint extends AbstractDelayEndpoint {

    private static final Log LOG = LogFactory.getLog(PersonEndpoint.class);

    private final ObjectMapper objectMapper;

    public PersonEndpoint(ObjectMapper objectMapper,
                          @Value("${delay.enabled}") boolean delayEnabled,
                          @Value("${delay.millis-person}") long delayMillis) {
        super(delayEnabled, delayMillis);
        this.objectMapper = objectMapper;
    }

    //https://open-education-api.github.io/specification/v5-rc/docs.html#tag/persons/paths/~1persons~1me/get
    @GetMapping(value = "/persons/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> personGet(BearerTokenAuthentication authentication) throws IOException {
        return doPerson(authentication);
    }

    @PostMapping(value = "/persons/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> personPost(BearerTokenAuthentication authentication) throws IOException {
        return doPerson(authentication);
    }

    private ResponseEntity<Map<String, Object>> doPerson(BearerTokenAuthentication authentication) throws IOException {
        this.delayResponse();
        Map<String, Object> map = objectMapper.readValue(new ClassPathResource("/data/person.json").getInputStream(), new TypeReference<Map<String, Object>>() {
        });

        Map<String, Object> tokenAttributes = authentication.getTokenAttributes();
        String eppn = (String) tokenAttributes.get("eduperson_principal_name");
        String givenName = (String) tokenAttributes.get("given_name");
        String familyName = (String) tokenAttributes.get("family_name");
        String mail = (String) tokenAttributes.get("email");

        LOG.debug(String.format("Persons request for person %s", eppn));

        map.put("personId", eppn);
        if (StringUtils.hasText(mail)) {
            map.put("mail", mail);
        }
        if (StringUtils.hasText(givenName)) {
            map.put("givenName", givenName);
        }
        if (StringUtils.hasText(familyName)) {
            map.put("surname", familyName);
        }
        if (StringUtils.hasText(givenName) && StringUtils.hasText(familyName)) {
            map.put("displayName", String.format("%s %s", givenName, familyName));
        }

        LOG.info("Returning in person endpoint for " + eppn);

        return ResponseEntity.ok(map);
    }
}
