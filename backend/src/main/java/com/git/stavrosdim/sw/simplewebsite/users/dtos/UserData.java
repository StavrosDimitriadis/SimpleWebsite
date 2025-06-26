package com.git.stavrosdim.sw.simplewebsite.users.dtos;

import jakarta.validation.constraints.*;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {

    private Long id;

    @NotNull(message = "Name is required")
    @Size(max = 20, message = "Name must be 20 characters maximum")
    private String name;

    @NotNull(message = "Surname is required")
    @Size(max = 20, message = "Surname must be 20 characters maximum")
    private String surname;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "M|F", message = "Gender must be 'M' (male) or 'F' (female)")
    private String gender;

    @NotNull(message = "Birthdate is required")
    @PastOrPresent(message = "Birthdate must be in the past or present")
    private Date birthdate;

    @Size(max = 100, message = "Home address must be 100 characters maximum")
    private String homeAddress;

    @Size(max = 100, message = "Work address must be 100 characters maximum")
    private String workAddress;
}
