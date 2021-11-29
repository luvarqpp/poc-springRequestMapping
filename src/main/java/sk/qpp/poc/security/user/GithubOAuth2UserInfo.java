package sk.qpp.poc.security.user;

import lombok.Getter;

import java.util.Map;

@Getter
public class GithubOAuth2UserInfo implements OAuth2UserInfo {
    private final String id;
    private final String name;
    private final String email;
    private final String imageUrl;

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        this.id = String.valueOf(attributes.get("id"));
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.imageUrl = (String) attributes.get("avatar_url");
    }
}
