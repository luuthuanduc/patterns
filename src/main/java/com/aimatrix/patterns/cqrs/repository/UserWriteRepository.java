package com.aimatrix.patterns.cqrs.repository;

import com.aimatrix.patterns.domain.User;

import java.util.HashMap;
import java.util.Map;

public class UserWriteRepository {

    private Map<String, User> store = new HashMap<>();

    public void addUser(String id, User user) {
        store.put(id, user);
    }

    public User getUser(String id) {
        return store.get(id);
    }

}
