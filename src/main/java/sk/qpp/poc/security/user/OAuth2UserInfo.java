package sk.qpp.poc.security.user;

/**
 * Interface for data returned in attributes from OAuth2 provider.
 * <p>
 * For example google does return id under "sub" attribute. So For google authenticated instance, you will get value
 * for "sub" key, when calling {@link #getId()} method. Another difference between github and google is
 * {@link #getImageUrl()} method, which does find its value in attributes
 * {@link org.springframework.security.oauth2.core.user.OAuth2User#getAttributes()} under "avatar_url" and "picture"
 * key respectively.
 */
public interface OAuth2UserInfo {
    String getId();

    String getName();

    String getEmail();

    String getImageUrl();
}
