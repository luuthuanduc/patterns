package com.aimatrix.cqrses.cqrs.aggregate;

import com.aimatrix.cqrses.cqrs.command.CreateUserCommand;
import com.aimatrix.cqrses.cqrs.command.UpdateUserCommand;
import com.aimatrix.cqrses.cqrs.repository.UserWriteRepository;
import com.aimatrix.cqrses.domain.User;

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
