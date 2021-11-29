package sk.qpp.poc.security.user;

import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    private final String id;
    private final String name;
    private final String email;
    private final String imageUrl;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.id = (String) attributes.get("sub");
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.imageUrl = (String) attributes.get("picture");
    }
}
