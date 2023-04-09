package com.aimatrix.patterns.cqrs.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserCommand {

    private String userId;
    private String firstName;
    private String lastName;

}
