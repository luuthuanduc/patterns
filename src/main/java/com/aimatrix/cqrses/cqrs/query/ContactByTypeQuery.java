package com.aimatrix.cqrses.cqrs.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactByTypeQuery {

    private String userId;
    private String contactType;
}
