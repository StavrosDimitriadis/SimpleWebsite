package com.git.stavrosdim.sw.simplewebsite.addresses;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity,Long>{

    Optional<AddressEntity> findByUserId(Long id);
}
