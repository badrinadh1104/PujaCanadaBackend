package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.CartList;
@Repository
public interface CartListRepository extends JpaRepository<CartList, Long>{

}
