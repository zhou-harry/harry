package com.harry.bpm.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmProctypeService;

@Repository("bpmProctypeService")
public class BpmProctypeRepository extends BaseRepository<BpmProctype> implements BpmProctypeService {

	public List<BpmProctype> findProctypeByParent(String parent) {
		String sql = "select t from BpmProctype t where t.parent=?1";

		Query query = em.createQuery(sql, BpmProctype.class);
		query.setParameter(1, parent);
		try {
			List<BpmProctype> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public List<BpmProctype> findDeploymentByUser(String userid) {
		String sql = "select * from T_BPM_PROC_TYPE t where exists(select 0 from ACT_RE_DEPLOYMENT d where t.TYPE_=d.TENANT_ID_ )";

		Query query = em.createNativeQuery(sql, BpmProctype.class);
//		query.setParameter(1, userid);
		try {
			List<BpmProctype> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public BpmProctype findByType(String type) {
		String sql = "select t from BpmProctype t where t.type=?1";

		Query query = em.createQuery(sql, BpmProctype.class);
		query.setParameter(1, type);
		try {
			return (BpmProctype)query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	

}
