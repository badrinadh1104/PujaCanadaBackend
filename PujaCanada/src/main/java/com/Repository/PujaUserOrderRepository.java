package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Model.PujaUserOrder;

@Repository
public interface PujaUserOrderRepository extends JpaRepository<PujaUserOrder,Long> {

}
