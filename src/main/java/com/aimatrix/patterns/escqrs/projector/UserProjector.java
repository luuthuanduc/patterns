package com.aimatrix.patterns.escqrs.projector;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.aimatrix.patterns.cqrs.repository.UserReadRepository;
import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import com.aimatrix.patterns.domain.UserAddress;
import com.aimatrix.patterns.domain.UserContact;
import com.aimatrix.patterns.es.event.Event;
import com.aimatrix.patterns.es.event.UserAddressAddedEvent;
import com.aimatrix.patterns.es.event.UserAddressRemovedEvent;
import com.aimatrix.patterns.es.event.UserContactAddedEvent;
import com.aimatrix.patterns.es.event.UserContactRemovedEvent;

public class UserProjector {

    UserReadRepository readRepository;

    public UserProjector(UserReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    public void project(String userId, List<Event> events) {

        for (Event event : events) {
            if (event instanceof UserAddressAddedEvent)
                apply(userId, (UserAddressAddedEvent) event);
            if (event instanceof UserAddressRemovedEvent)
                apply(userId, (UserAddressRemovedEvent) event);
            if (event instanceof UserContactAddedEvent)
                apply(userId, (UserContactAddedEvent) event);
            if (event instanceof UserContactRemovedEvent)
                apply(userId, (UserContactRemovedEvent) event);
        }

    }

    public void apply(String userId, UserAddressAddedEvent event) {
        Address address = new Address(event.getCity(), event.getState(), event.getPostCode());
        UserAddress userAddress = Optional.ofNullable(readRepository.getUserAddress(userId))
            .orElse(new UserAddress());
        Set<Address> addresses = Optional.ofNullable(userAddress.getAddressByRegion()
            .get(address.getState()))
            .orElse(new HashSet<>());
        addresses.add(address);
        userAddress.getAddressByRegion()
            .put(address.getState(), addresses);
        readRepository.addUserAddress(userId, userAddress);
    }

    public void apply(String userId, UserAddressRemovedEvent event) {
        Address address = new Address(event.getCity(), event.getState(), event.getPostCode());
        UserAddress userAddress = readRepository.getUserAddress(userId);
        if (userAddress != null) {
            Set<Address> addresses = userAddress.getAddressByRegion()
                .get(address.getState());
            if (addresses != null)
                addresses.remove(address);
            readRepository.addUserAddress(userId, userAddress);
        }
    }

    public void apply(String userId, UserContactAddedEvent event) {
        Contact contact = new Contact(event.getContactType(), event.getContactDetails());
        UserContact userContact = Optional.ofNullable(readRepository.getUserContact(userId))
            .orElse(new UserContact());
        Set<Contact> contacts = Optional.ofNullable(userContact.getContactByType()
            .get(contact.getType()))
            .orElse(new HashSet<>());
        contacts.add(contact);
        userContact.getContactByType()
            .put(contact.getType(), contacts);
        readRepository.addUserContact(userId, userContact);
    }

    public void apply(String userId, UserContactRemovedEvent event) {
        Contact contact = new Contact(event.getContactType(), event.getContactDetails());
        UserContact userContact = readRepository.getUserContact(userId);
        if (userContact != null) {
            Set<Contact> contacts = userContact.getContactByType()
                .get(contact.getType());
            if (contacts != null)
                contacts.remove(contact);
            readRepository.addUserContact(userId, userContact);
        }
    }
}
