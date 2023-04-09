package com.aimatrix.cqrses.cqrs;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.aimatrix.cqrses.cqrs.aggregate.UserAggregate;
import com.aimatrix.cqrses.cqrs.command.CreateUserCommand;
import com.aimatrix.cqrses.cqrs.command.UpdateUserCommand;
import com.aimatrix.cqrses.cqrs.projection.UserProjection;
import com.aimatrix.cqrses.cqrs.projector.UserProjector;
import com.aimatrix.cqrses.cqrs.query.AddressByRegionQuery;
import com.aimatrix.cqrses.cqrs.query.ContactByTypeQuery;
import com.aimatrix.cqrses.cqrs.repository.UserReadRepository;
import com.aimatrix.cqrses.cqrs.repository.UserWriteRepository;
import com.aimatrix.cqrses.domain.Address;
import com.aimatrix.cqrses.domain.Contact;
import com.aimatrix.cqrses.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatternsApplicationUnitTest {

    private UserWriteRepository writeRepository;
    private UserReadRepository readRepository;
    private UserProjector projector;
    private UserAggregate userAggregate;
    private UserProjection userProjection;

    @BeforeEach
    public void setUp() {
        writeRepository = new UserWriteRepository();
        readRepository = new UserReadRepository();
        projector = new UserProjector(readRepository);
        userAggregate = new UserAggregate(writeRepository);
        userProjection = new UserProjection(readRepository);
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String userId = UUID.randomUUID()
            .toString();
        User user = null;
        CreateUserCommand createUserCommand = new CreateUserCommand(userId, "Tom", "Sawyer");
        user = userAggregate.handleCreateUserCommand(createUserCommand);
        projector.project(user);

        UpdateUserCommand updateUserCommand = new UpdateUserCommand(user.getUserid(),
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Los Angeles", "CA", "90001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("EMAIL", "tom.sawyer@rediff.com")
            ).collect(Collectors.toSet()));
        user = userAggregate.handleUpdateUserCommand(updateUserCommand);
        projector.project(user);

        updateUserCommand = new UpdateUserCommand(userId,
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Housten", "TX", "77001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("PHONE", "700-000-0001")
            ).collect(Collectors.toSet()));
        user = userAggregate.handleUpdateUserCommand(updateUserCommand);
        projector.project(user);

        ContactByTypeQuery contactByTypeQuery = new ContactByTypeQuery(userId, "EMAIL");
        Assertions.assertEquals(
            Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"))
                .collect(Collectors.toSet()),
            userProjection.handle(contactByTypeQuery)
        );
        AddressByRegionQuery addressByRegionQuery = new AddressByRegionQuery(userId, "NY");
        Assertions.assertEquals(
            Stream.of(new Address("New York", "NY", "10001"))
                .collect(Collectors.toSet()),
            userProjection.handle(addressByRegionQuery)
        );
    }

}
