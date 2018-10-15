package com.harry.fssc.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.harry.fssc.model.Attachment;


public interface AttachmentRepository extends JpaRepository<Attachment, String> {

	Optional<Attachment> findById(String id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update Attachment set state=:state where id=:id")
	int updateState(@Param("id") String id,@Param("state") int state);
}
