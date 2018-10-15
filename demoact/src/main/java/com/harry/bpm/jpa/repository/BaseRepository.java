package com.harry.bpm.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BaseService;

public class BaseRepository<T> implements BaseService<T> {

	@PersistenceContext
	protected EntityManager em;

	@Transactional
	public T save(T t) {
		T r = null;
		if (null != t) {
			r = this.em.merge(t);
		}
		em.close();
		return r;
	}

	@Transactional
	public void save(List<T> ts) {
		if (null != ts && ts.size() > 0) {
			for (int i = 0; i < ts.size(); i++) {
				this.em.merge(ts.get(i));
			}
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> c) {
		Table t = c.getAnnotation(Table.class);

		String sql = "select * from " + t.schema() + "." + t.name();

		Query query = em.createNativeQuery(sql, c);

		try {
			List<T> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	@Transactional
	public void delete(T t) {
		if (null != t) {
			this.em.remove(em.contains(t) ? t : em.merge(t));
		}
		em.flush();
		em.close();
	}

}
