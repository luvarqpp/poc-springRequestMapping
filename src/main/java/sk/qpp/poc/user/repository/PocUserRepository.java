package sk.qpp.poc.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource
@Transactional(readOnly = true)
public interface PocUserRepository extends PagingAndSortingRepository<PocUser, Long> {
    Page<PocUser> findByDisplayNameContaining(String displayNamePart, Pageable pageable);

    /**
     * Needed by security things.
     */
    Optional<PocUser> findByDisplayName(String exactDisplayName);

    /**
     * Needed by security things.
     */
    Optional<PocUser> findByEmail(String email);

    /**
     * Needed by security things.
     */
    Optional<PocUser> findByDisplayNameOrEmail(String displayName, String email);

    /**
     * Needed by security things.
     */
    List<PocUser> findByIdIn(List<Long> userIds);

    /**
     * Needed by security things.
     */
    Boolean existsByDisplayName(String displayName);

    /**
     * Needed by security things.
     */
    Boolean existsByEmail(String email);

    @Override
    @RestResource(exported = false)
    @Transactional
    <S extends PocUser> S save(S s);

    @Override
    @RestResource(exported = false)
    @Transactional
    <S extends PocUser> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    @Transactional
    void delete(PocUser PocUser);

    @Override
    @RestResource(exported = false)
    @Transactional
    void deleteAll();

    @Override
    @RestResource(exported = false)
    @Transactional
    void deleteAll(Iterable<? extends PocUser> iterable);

    @Override
    @RestResource(exported = false)
    @Transactional
    void deleteById(Long aLong);

    /**
     * Update of password is possible with internal database ID and email only.
     *
     * @param id                      id of {@link PocUser} row in database
     * @param email                   email of given {@link PocUser}
     * @param newPasswordSaltedHashed new password value. It will be directly saved to database
     * @return New instance of {@link sk.qpp.poc.security.UserPrincipal} with changed password value.
     */
    @Modifying // TODO should we request old password value here? What about "forgot password"?
    @Query("update PocUser u set u.passwordSaltedHashed = :passwordSaltedHashed where u.email = :email AND u.id = :id")
    @Transactional
    int updateUsersPassword(@Param("id") Long id, @Param("email") String email, @Param("passwordSaltedHashed") String newPasswordSaltedHashed);
}
