package com.git.stavrosdim.sw.simplewebsite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.git.stavrosdim.sw.simplewebsite.addresses.AddressEntity;
import com.git.stavrosdim.sw.simplewebsite.addresses.AddressRepository;
import com.git.stavrosdim.sw.simplewebsite.users.UsersEntity;
import com.git.stavrosdim.sw.simplewebsite.users.UsersRepository;
import com.git.stavrosdim.sw.simplewebsite.users.UsersService;
import com.git.stavrosdim.sw.simplewebsite.users.dtos.UserData;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomExceptions;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private UsersService usersService;

    // Add User Tests

    @Test
    public void add_User_With_Null_Addresses() {

        // Arrange
        final var userData = inputUserData(null, null);
        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);

        // Act
        usersService.addUserData(userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository, never()).save(any());
        assertUserEntity(userCaptor);
    }

    @ParameterizedTest
    @MethodSource("addressesDataset")
    public void add_User_With_Addresses(String homeAddress, String workAddress) {

        // Arrange
        final var userData = inputUserData(homeAddress, workAddress);
        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);
        ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);

        // Act
        usersService.addUserData(userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository).save(addressCaptor.capture());
        assertUserEntity(userCaptor);
        assertAddressEntity(addressCaptor, homeAddress, workAddress, userCaptor.getValue());
    }

    // Delete User Tests

    @Test
    public void delete_Existing_User() {

        // Arrange
        final var userId = 5L;
        final var user = new UsersEntity();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        usersService.deleteUserById(userId);

        // Assert
        verify(usersRepository).delete(user);
    }

    @Test
    public void delete_Not_Existing_User() {

        // Arrange
        final var userId = 5L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            usersService.deleteUserById(userId);
        });
    }

    // Update User Tests

    @Test
    public void update_Not_Existing_User() {

        // Arrange
        final var userId = 5L;
        final var userData = new UserData();
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            usersService.updateUser(userId, userData);
        });
    }

    @Test
    public void update_User_With_Existing_AddressEntity_With_Null_Addresses() {

        // Arrange
        final var userId = 5L;
        final var userData = inputUserData(null, null);
        final var userEntity = previousUserEntity();
        final var addressEntity = previousAddressEntity(userEntity);

        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);
        ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(addressRepository.findByUserId(userId)).thenReturn(Optional.of(addressEntity));

        // Act
        usersService.updateUser(userId, userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository).save(addressCaptor.capture());
        assertUserEntity(userCaptor);
        assertAddressEntity(addressCaptor, null, null, userEntity);
    }

    @ParameterizedTest
    @MethodSource("addressesDataset")
    public void update_User_With_Existing_AddressEntity_With_Addresses(String homeAddress, String workAddress) {

        // Arrange
        final var userId = 5L;
        final var userData = inputUserData(homeAddress, workAddress);
        final var userEntity = previousUserEntity();
        final var addressEntity = previousAddressEntity(userEntity);

        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);
        ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(addressRepository.findByUserId(userId)).thenReturn(Optional.of(addressEntity));

        // Act
        usersService.updateUser(userId, userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository).save(addressCaptor.capture());
        assertUserEntity(userCaptor);
        assertAddressEntity(addressCaptor, homeAddress, workAddress, userEntity);
    }

    @Test
    public void update_User_Without_Existing_Address_Entity_With_Null_Addresses() {

        // Arrange
        final var userId = 5L;
        final var userData = inputUserData(null, null);
        final var userEntity = previousUserEntity();

        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(addressRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        usersService.updateUser(userId, userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository, never()).save(any());
        assertUserEntity(userCaptor);
    }

    @ParameterizedTest
    @MethodSource("addressesDataset")
    public void update_User_Without_Existing_Address_Entity_With_Addresses(String homeAddress, String workAddress) {

        // Arrange
        final var userId = 5L;
        final var userData = inputUserData(homeAddress, workAddress);
        final var userEntity = previousUserEntity();

        ArgumentCaptor<UsersEntity> userCaptor = ArgumentCaptor.forClass(UsersEntity.class);
        ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(addressRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        usersService.updateUser(userId, userData);

        // Assert
        verify(usersRepository).save(userCaptor.capture());
        verify(addressRepository).save(addressCaptor.capture());
        assertUserEntity(userCaptor);
        assertAddressEntity(addressCaptor, homeAddress, workAddress, userEntity);
    }

    static Stream<Arguments> addressesDataset() {
        return Stream.of(
                Arguments.of("Karamanli", "Andrianoupoleos"),
                Arguments.of("Karamanli", null),
                Arguments.of(null, "Andrianoupoleos"));
    }

    private UserData inputUserData(String homeAddress, String workAddress) {
        return UserData.builder()
                .name("Giannis")
                .surname("Papadopoulos")
                .gender("M")
                .birthdate(Date.valueOf("2020-05-05"))
                .homeAddress(homeAddress)
                .workAddress(workAddress)
                .build();
    }

    private UsersEntity previousUserEntity() {
        return UsersEntity.builder()
                .name("PreviousName")
                .surname("PreviousSurname")
                .gender("F")
                .birthdate(Date.valueOf("1980-01-01"))
                .build();
    }

    private AddressEntity previousAddressEntity(UsersEntity user) {
        return AddressEntity.builder()
                .homeAddress("PreviousHomeAddress")
                .workAddress("PreviousWorkAddress")
                .user(user)
                .build();
    }

    private void assertUserEntity(ArgumentCaptor<UsersEntity> userCaptor) {
        assertEquals("Giannis", userCaptor.getValue().getName());
        assertEquals("Papadopoulos", userCaptor.getValue().getSurname());
        assertEquals("M", userCaptor.getValue().getGender());
        assertEquals(Date.valueOf("2020-05-05"), userCaptor.getValue().getBirthdate());
    }

    private void assertAddressEntity(ArgumentCaptor<AddressEntity> addressCaptor, String expectedHomeAddress,
            String expectedWorkAddress, UsersEntity expectedUser) {
        assertEquals(expectedHomeAddress, addressCaptor.getValue().getHomeAddress());
        assertEquals(expectedWorkAddress, addressCaptor.getValue().getWorkAddress());
        assertEquals(expectedUser, addressCaptor.getValue().getUser());
    }
}
