package com.aimatrix.patterns.crud.service;

import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import com.aimatrix.patterns.domain.User;
import com.aimatrix.patterns.crud.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void createUser(String userId, String firstName, String lastName) {
        User user = new User(userId, firstName, lastName);
        repository.addUser(userId, user);
    }

    public void updateUser(String userId, Set<Contact> contacts, Set<Address> addresses) throws Exception {
        User user = repository.getUser(userId);
        if (user == null)
            throw new Exception("User does not exist.");
        user.setContacts(contacts);
        user.setAddresses(addresses);
        repository.addUser(userId, user);
    }

    public Set<Contact> getContactByType(String userId, String contactType) throws Exception {
        User user = repository.getUser(userId);
        if (user == null)
            throw new Exception("User does not exit.");
        Set<Contact> contacts = user.getContacts();
        return contacts.stream()
            .filter(contact -> contact.getType()
                .equals(contactType))
            .collect(Collectors.toSet());
    }

    public Set<Address> getAddressByRegion(String userId, String state) throws Exception {
        User user = repository.getUser(userId);
        if (user == null)
            throw new Exception("User does not exist.");
        Set<Address> addresses = user.getAddresses();
        return addresses.stream()
            .filter(address -> address.getState()
                .equals(state))
            .collect(Collectors.toSet());
    }

}
