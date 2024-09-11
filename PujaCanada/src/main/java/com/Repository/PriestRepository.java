package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.Priest;
@Repository
public interface PriestRepository extends JpaRepository<Priest, Long> {

//	Priest findByEmail(String email);

}
