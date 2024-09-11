package com.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.PujaAppointment;

@Repository
public interface PujaAppointmentRepository extends JpaRepository<PujaAppointment, Long>{

	List<PujaAppointment> findByPujaId(Long userId);

}
