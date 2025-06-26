package com.git.stavrosdim.sw.simplewebsite.users;

import java.sql.Date;
import org.hibernate.annotations.Check;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String surname;

    @Column(nullable = false, columnDefinition = "CHAR(1)")
    @Check(constraints = "gender IN ('M', 'F')")
    private String gender;

    @Column(nullable = false)
    private Date birthdate;
}
