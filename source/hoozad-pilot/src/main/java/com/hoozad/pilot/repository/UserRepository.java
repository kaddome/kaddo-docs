package com.hoozad.pilot.repository;


import com.hoozad.pilot.domain.ExternalAccountProvider;
import com.hoozad.pilot.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByLogin(String login);

    void delete(User t);

    @Query("{externalAccounts: { $in: [ {externalProvider: ?0, externalId: ?1} ]}}")
    Optional<User> getUserByExternalAccount(ExternalAccountProvider provider, String externalAccountId);

}
