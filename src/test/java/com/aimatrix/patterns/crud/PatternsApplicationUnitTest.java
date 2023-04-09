package com.aimatrix.patterns.crud;

import com.aimatrix.patterns.domain.Address;
import com.aimatrix.patterns.domain.Contact;
import com.aimatrix.patterns.crud.repository.UserRepository;
import com.aimatrix.patterns.crud.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatternsApplicationUnitTest {

    private UserRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new UserRepository();
    }

    @Test
    public void givenCRUDApplication_whenDataCreated_thenDataCanBeFetched() throws Exception {
        UserService service = new UserService(repository);
        String userId = UUID.randomUUID()
            .toString();

        service.createUser(userId, "Tom", "Sawyer");
        service.updateUser(userId,
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("EMAIL", "tom.sawyer@rediff.com"),
                new Contact("PHONE", "700-000-0001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Los Angeles", "CA", "90001"),
                new Address("Housten", "TX", "77001")
            ).collect(Collectors.toSet()));

        service.updateUser(userId,
            Stream.of(
                new Contact("EMAIL", "tom.sawyer@gmail.com"),
                new Contact("PHONE", "700-000-0001")
            ).collect(Collectors.toSet()),
            Stream.of(
                new Address("New York", "NY", "10001"),
                new Address("Housten", "TX", "77001")
            ).collect(Collectors.toSet()));

        Assertions.assertEquals(
            Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"))
                .collect(Collectors.toSet()),
            service.getContactByType(userId, "EMAIL")
        );
        Assertions.assertEquals(
            Stream.of(new Address("New York", "NY", "10001"))
                .collect(Collectors.toSet()),
            service.getAddressByRegion(userId, "NY")
        );
    }

}
