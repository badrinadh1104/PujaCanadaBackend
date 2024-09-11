package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long>{

}
