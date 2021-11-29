package sk.qpp.poc.security.user;

import sk.qpp.poc.security.exception.OAuth2AuthenticationProcessingException;
import sk.qpp.poc.user.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        final AuthProvider authProviderEnum = AuthProvider.valueOf(registrationId.toUpperCase());
        switch (authProviderEnum) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case GITHUB:
                return new GithubOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
