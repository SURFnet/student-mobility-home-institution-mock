package home.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class User {

    private final String eppn;
    private final String eduid;
    private final String givenName;
    private final String familyName;
    private final String email;

    public User(Map<String, Object> tokenAttributes) {
        this.eppn = (String) tokenAttributes.get("eduperson_principal_name");
        this.eduid = (String) tokenAttributes.get("eduid");
        this.givenName = (String) tokenAttributes.get("given_name");
        this.familyName = (String) tokenAttributes.get("family_name");
        this.email = (String) tokenAttributes.get("email");

    }
}
