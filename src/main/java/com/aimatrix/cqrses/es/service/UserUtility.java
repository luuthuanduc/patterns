package com.aimatrix.cqrses.es.service;

import java.util.List;
import java.util.UUID;

import com.aimatrix.cqrses.domain.Address;
import com.aimatrix.cqrses.domain.Contact;
import com.aimatrix.cqrses.domain.User;
import com.aimatrix.cqrses.es.event.Event;
import com.aimatrix.cqrses.es.event.UserAddressAddedEvent;
import com.aimatrix.cqrses.es.event.UserAddressRemovedEvent;
import com.aimatrix.cqrses.es.event.UserContactAddedEvent;
import com.aimatrix.cqrses.es.event.UserContactRemovedEvent;
import com.aimatrix.cqrses.es.event.UserCreatedEvent;
import com.aimatrix.cqrses.es.repository.EventStore;

public class UserUtility {

    public static User recreateUserState(EventStore store, String userId) {
        User user = null;

        List<Event> events = store.getEvents(userId);
        for (Event event : events) {
            if (event instanceof UserCreatedEvent) {
                UserCreatedEvent e = (UserCreatedEvent) event;
                user = new User(UUID.randomUUID()
                    .toString(), e.getFirstName(), e.getLastName());
            }
            if (event instanceof UserAddressAddedEvent) {
                UserAddressAddedEvent e = (UserAddressAddedEvent) event;
                Address address = new Address(e.getCity(), e.getState(), e.getPostCode());
                if (user != null)
                    user.getAddresses()
                        .add(address);
            }
            if (event instanceof UserAddressRemovedEvent) {
                UserAddressRemovedEvent e = (UserAddressRemovedEvent) event;
                Address address = new Address(e.getCity(), e.getState(), e.getPostCode());
                if (user != null)
                    user.getAddresses()
                        .remove(address);
            }
            if (event instanceof UserContactAddedEvent) {
                UserContactAddedEvent e = (UserContactAddedEvent) event;
                Contact contact = new Contact(e.getContactType(), e.getContactDetails());
                if (user != null)
                    user.getContacts()
                        .add(contact);
            }
            if (event instanceof UserContactRemovedEvent) {
                UserContactRemovedEvent e = (UserContactRemovedEvent) event;
                Contact contact = new Contact(e.getContactType(), e.getContactDetails());
                if (user != null)
                    user.getContacts()
                        .remove(contact);
            }
        }

        return user;
    }

}
