package com.git.stavrosdim.sw.simplewebsite.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.git.stavrosdim.sw.simplewebsite.users.dtos.UserData;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomExceptions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/storeUser")
    public void storeUser(@Valid @RequestBody(required = false) UserData userData) {
        if (userData == null) {
            throw new CustomExceptions.EmptyRequestBodyException();
        }
        usersService.addUserData(userData);
    }

    @GetMapping("/fetchUsers")
    public Page<UserData> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be 0 or greater") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be 1 or greater") int size) {
        return usersService.getAllUsers(PageRequest.of(page, size));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        usersService.deleteUserById(id);
    }

    @PutMapping("/updateUser/{id}")
    public void updateUser(@PathVariable Long id, @Valid @RequestBody(required = false) UserData user) {
        if (user == null) {
            throw new CustomExceptions.EmptyRequestBodyException();
        }
        usersService.updateUser(id, user);
    }
}
