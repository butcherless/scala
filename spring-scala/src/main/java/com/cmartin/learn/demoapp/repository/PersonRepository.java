package com.cmartin.learn.demoapp.repository;

import com.cmartin.learn.demoapp.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import scala.Option;

public interface PersonRepository extends Neo4jRepository<User, Long> {
    User findByEmail(String email);

    Option<User> findByFirstName(String firstName);
}
