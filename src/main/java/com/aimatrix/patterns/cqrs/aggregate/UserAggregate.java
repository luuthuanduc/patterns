package com.aimatrix.patterns.cqrs.aggregate;

import com.aimatrix.patterns.cqrs.command.CreateUserCommand;
import com.aimatrix.patterns.cqrs.command.UpdateUserCommand;
import com.aimatrix.patterns.cqrs.repository.UserWriteRepository;
import com.aimatrix.patterns.domain.User;

public class UserAggregate {

    private UserWriteRepository writeRepository;

    public UserAggregate(UserWriteRepository repository) {
        this.writeRepository = repository;
    }

    public User handleCreateUserCommand(CreateUserCommand command) {
        User user = new User(command.getUserId(), command.getFirstName(), command.getLastName());
        writeRepository.addUser(user.getUserid(), user);
        return user;
    }

    public User handleUpdateUserCommand(UpdateUserCommand command) {
        User user = writeRepository.getUser(command.getUserId());
        user.setAddresses(command.getAddresses());
        user.setContacts(command.getContacts());
        writeRepository.addUser(user.getUserid(), user);
        return user;
    }

}
