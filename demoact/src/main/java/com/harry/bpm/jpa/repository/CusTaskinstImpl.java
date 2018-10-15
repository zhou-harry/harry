package com.harry.bpm.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.CusTaskinst;
import com.harry.bpm.jpa.service.CusTaskinstService;

@Repository("cusTaskinstService")
public class CusTaskinstImpl implements CusTaskinstService {

	@PersistenceContext
	protected EntityManager em;

	@Transactional
	public void save(CusTaskinst bean) {

		if (null != bean) {
			this.em.merge(bean);
		}
		em.close();
	}
}
