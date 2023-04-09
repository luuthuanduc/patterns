package com.aimatrix.patterns.escqrs;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.aimatrix.patterns.cqrs.command.CreateUserCommand;
import com.aimatrix.patterns.cqrs.command.UpdateUserCommand;
import com.aimatrix.patterns.cqrs.projection.UserProjection;
import com.aimatrix.patterns.cqrs.query.AddressByRegionQuery;
import com.aimatrix.patterns.cqrs.query.ContactByTypeQuery;
import com.aimatrix.patterns.cqrs.repository.UserReadRepository;
import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import com.aimatrix.patterns.es.event.Event;
import com.aimatrix.patterns.es.repository.EventStore;
import com.aimatrix.patterns.escqrs.aggregate.UserAggregate;
import com.aimatrix.patterns.escqrs.projector.UserProjector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatternsApplicationUnitTest {

    private EventStore writeRepository;
    private UserReadRepository readRepository;
    private UserProjector projector;
    private UserAggregate userAggregate;
    private UserProjection userProjection;

    @BeforeEach
    public void setUp() {
        writeRepository = new EventStore();
        readRepository = new UserReadRepository();
        projector = new UserProjector(readRepository);
        userAggregate = new UserAggregate(writeRepository);
        userProjection = new UserProjection(readRepository);
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String userId = UUID.randomUUID()
            .toString();
        List<Event> events = null;
        CreateUserCommand createUserCommand = new CreateUserCommand(userId, "Kumar", "Chandrakant");
        events = userAggregate.handleCreateUserCommand(createUserCommand);

        projector.project(userId, events);

        UpdateUserCommand updateUserCommand = new UpdateUserCommand(userId,
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Los Angeles", "CA", "90001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("EMAIL", "tom.sawyer@rediff.com")
            ).collect(Collectors.toSet()));
        events = userAggregate.handleUpdateUserCommand(updateUserCommand);
        projector.project(userId, events);

        updateUserCommand = new UpdateUserCommand(userId,
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Housten", "TX", "77001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("PHONE", "700-000-0001")
            ).collect(Collectors.toSet()));
        events = userAggregate.handleUpdateUserCommand(updateUserCommand);
        projector.project(userId, events);

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
