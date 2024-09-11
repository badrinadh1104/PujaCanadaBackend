package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
