package com.git.stavrosdim.sw.simplewebsite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
    private String name;
    private String surname;
    private String gender;
    private String birthdate;
    private String workAddress;
    private String homeAddress;
}
