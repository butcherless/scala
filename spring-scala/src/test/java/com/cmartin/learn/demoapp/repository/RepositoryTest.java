package com.cmartin.learn.demoapp.repository;

import com.cmartin.learn.demoapp.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class RepositoryTest {
    @Rule
    public Neo4jRule neoServer = new Neo4jRule();
    private Session session;

    @Before
    public void setUp() throws Exception {

        Configuration configuration = new Configuration.Builder()
                .uri(neoServer.boltURI().toString())
                //.uri(neoServer.httpURI().toString()) // For HTTP
                //.uri(new File("target/graph.db").toURI().toString()) // For Embedded
                .build();

        SessionFactory sessionFactory = new SessionFactory(configuration, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        session.purgeDatabase();
    }

    @Test
    public void sampleTest() {
        User user = new User("noone@nowhere.com", "No", "One");
        session.save(user);

        Collection<User> allUsers = session.loadAll(User.class);
        assertThat(allUsers).hasSize(10);

        assertThat(allUsers.iterator().next().getEmail()).isEqualTo("noone@nowhere.com");
    }
}
