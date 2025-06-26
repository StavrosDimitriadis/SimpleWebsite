package com.git.stavrosdim.sw.simplewebsite.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.git.stavrosdim.sw.simplewebsite.addresses.AddressEntity;
import com.git.stavrosdim.sw.simplewebsite.addresses.AddressRepository;
import com.git.stavrosdim.sw.simplewebsite.users.dtos.UserData;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomExceptions;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final AddressRepository addressRepository;

    public UsersService(UsersRepository usersRepository, AddressRepository addressRepository) {
        this.usersRepository = usersRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void addUserData(UserData userData) {

        final var user = UsersEntity
                .builder()
                .name(userData.getName())
                .surname(userData.getSurname())
                .gender(userData.getGender())
                .birthdate(userData.getBirthdate()).build();

        usersRepository.save(user);

        storeAddressData(user, userData.getHomeAddress(), userData.getWorkAddress());
    }

    public Page<UserData> getAllUsers(Pageable pageable) {
        return usersRepository.fetchUsers(pageable);
    }

    @Transactional
    public void deleteUserById(Long id) {
        final var user = checkIfUserExists(id);
        usersRepository.delete(user);
    }

    @Transactional
    public void updateUser(Long id, UserData user) {

        final var userEntity = checkIfUserExists(id);

        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setGender(user.getGender());
        userEntity.setBirthdate(user.getBirthdate());

        usersRepository.save(userEntity);

        addressRepository.findByUserId(id).ifPresentOrElse(
                addressEntity -> {
                    addressEntity.setHomeAddress(user.getHomeAddress());
                    addressEntity.setWorkAddress(user.getWorkAddress());
                    addressRepository.save(addressEntity);
                },
                () -> {
                    storeAddressData(userEntity, user.getHomeAddress(), user.getWorkAddress());
                });
    }

    private void storeAddressData(UsersEntity user, String homeAddress, String workAddress) {

        if (homeAddress != null || workAddress != null) {

            final var addressData = AddressEntity
                    .builder()
                    .homeAddress(homeAddress)
                    .workAddress(workAddress)
                    .user(user)
                    .build();

            addressRepository.save(addressData);
        }
    }

    private UsersEntity checkIfUserExists(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException(id));
    }
}
