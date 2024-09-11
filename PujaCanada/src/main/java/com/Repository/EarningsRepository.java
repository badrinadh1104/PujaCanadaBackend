package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Model.Earnings;

@Repository
public interface EarningsRepository extends JpaRepository<Earnings, Long> {

}
