package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dto.ImportStatus;

public interface ImportStatusRepository extends JpaRepository<ImportStatus, Long> {

	boolean existsByImportType(String string);

}
