package com.hoozad.pilot.repository;

import com.hoozad.pilot.domain.Foo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Foo entity.
 */
public interface FooRepository extends MongoRepository<Foo,String> {

}
