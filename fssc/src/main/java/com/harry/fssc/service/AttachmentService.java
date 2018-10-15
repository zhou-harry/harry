package com.harry.fssc.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.Attachment;
import com.harry.fssc.repository.AttachmentRepository;
@Component
public class AttachmentService {

	private static Logger logger = LoggerFactory.getLogger(AttachmentService.class);

	@Autowired
	private AttachmentRepository repository;
	
	public boolean save(Attachment entity) {
		repository.saveAndFlush(entity);
		return true;
	}
	
	public Optional<Attachment> findById(String id) {
		return repository.findById(id);
	}
	
	public int updateState(String id,int state) {
		return repository.updateState(id, state);
	}
}
