package com.aimatrix.patterns.cqrs.command;

import java.util.HashSet;
import java.util.Set;

import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserCommand {

    private String userId;
    private Set<Address> addresses = new HashSet<>();
    private Set<Contact> contacts = new HashSet<>();

}
