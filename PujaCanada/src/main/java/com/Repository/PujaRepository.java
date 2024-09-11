package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.Puja;
@Repository
public interface PujaRepository extends JpaRepository<Puja, Long> {

	Puja findByName(String name);

}
