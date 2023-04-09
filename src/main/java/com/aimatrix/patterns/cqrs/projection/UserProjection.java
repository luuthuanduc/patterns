package com.aimatrix.patterns.cqrs.projection;

import com.aimatrix.patterns.cqrs.query.AddressByRegionQuery;
import com.aimatrix.patterns.cqrs.query.ContactByTypeQuery;
import com.aimatrix.patterns.cqrs.repository.UserReadRepository;
import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import com.aimatrix.patterns.domain.UserAddress;
import com.aimatrix.patterns.domain.UserContact;

import java.util.Set;

public class UserProjection {

    private UserReadRepository repository;

    public UserProjection(UserReadRepository repository) {
        this.repository = repository;
    }

    public Set<Contact> handle(ContactByTypeQuery query) throws Exception {
        UserContact userContact = repository.getUserContact(query.getUserId());
        if (userContact == null)
            throw new Exception("User does not exist.");
        return userContact.getContactByType()
            .get(query.getContactType());
    }

    public Set<Address> handle(AddressByRegionQuery query) throws Exception {
        UserAddress userAddress = repository.getUserAddress(query.getUserId());
        if (userAddress == null)
            throw new Exception("User does not exist.");
        return userAddress.getAddressByRegion()
            .get(query.getState());
    }

}
