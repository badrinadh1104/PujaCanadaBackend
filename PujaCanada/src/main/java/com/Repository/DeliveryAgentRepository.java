package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.DeliveryAgent;
@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {

}
