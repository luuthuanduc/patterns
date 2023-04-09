package com.aimatrix.cqrses.es.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserContactAddedEvent extends Event {

    private String contactType;
    private String contactDetails;

}
