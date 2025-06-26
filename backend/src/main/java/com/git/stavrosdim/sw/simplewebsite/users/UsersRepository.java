package com.git.stavrosdim.sw.simplewebsite.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.git.stavrosdim.sw.simplewebsite.users.dtos.UserData;

public interface UsersRepository extends JpaRepository<UsersEntity,Long> {

    @Query("""
        SELECT new com.git.stavrosdim.sw.simplewebsite.users.dtos.UserData(
            u.id, u.name, u.surname, u.gender, u.birthdate, a.homeAddress, a.workAddress)
        FROM UsersEntity u
        LEFT JOIN AddressEntity a ON u = a.user
        """)
    Page<UserData> fetchUsers(Pageable pageable);
}
