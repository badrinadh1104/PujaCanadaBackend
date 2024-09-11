package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.WishList;
@Repository
public interface WishListRepository extends JpaRepository<WishList, Long>{

}
