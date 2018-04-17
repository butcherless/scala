package com.cmartin.learn.demoapp.domain;


import org.neo4j.ogm.annotation.*;

@NodeEntity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "firstName")
    private String firstName;

    private String lastName;

    @Index(unique = true)
    private String email;

    public User() {
        // required by
    }

    public User(String email, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
