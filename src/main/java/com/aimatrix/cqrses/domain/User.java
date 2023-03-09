package com.aimatrix.cqrses.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    @NonNull
    private String userid;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    private Set<Contact> contacts = new HashSet<>();
    private Set<Address> addresses = new HashSet<>();

}