package com.harry.bpm.jpa.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmFilter;
import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.jpa.service.BpmFilterService;

@Repository("bpmFilterService")
public class BpmFilterRepository extends BaseRepository<BpmFilter> implements BpmFilterService {

	public Map<String, Object> findByKey(String key) {
		Map<String, Object>result=new HashMap<String, Object>();
		String sql = "select f.*,m.NAME_ M_NAME,s.NAME_ S_NAME from T_BPM_FILTER f left join T_BPM_FILTER m on f.MASTER=m.FILTER_ID left join T_BPM_FILTER s on f.SLAVE=s.FILTER_ID where f.FILTER_ID=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, key);
		try {
			Object[] os = (Object[])query.getSingleResult();
			result.put("ID", os[0]);
			result.put("filterId", os[1]);
			result.put("name", os[2]);
			result.put("type", os[3]);
			result.put("master", os[4]);
			result.put("slave", os[5]);
			result.put("relation", os[6]);
			result.put("masterName", os[7]);
			result.put("slaveName", os[8]);
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
		return result;
	}

	public List<BpmFilter> findForRelation(String key) {
		String sql = "SELECT * FROM T_BPM_FILTER f where f.FILTER_ID not in(SELECT FILTER_ID FROM T_BPM_FILTER START WITH FILTER_ID = ? CONNECT BY NOCYCLE PRIOR FILTER_ID in(MASTER,SLAVE))";
		Query query = em.createNativeQuery(sql, BpmFilter.class);
		query.setParameter(1, key);
		try {
			List<BpmFilter> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public BpmFilter findFilterBykey(String filterId) {
		String sql = "select t from BpmFilter t where t.filterId=?1";

		Query query = em.createQuery(sql, BpmFilter.class);
		query.setParameter(1, filterId);
		try {
			Object obj = query.getSingleResult();
			if (null!=obj) {
				return (BpmFilter)obj;
			}
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
		return null;
	}

	public String findNameByKey(String filterId) {
		String sql = "select f.name from BpmFilter f where f.filterId=?1";
		Query query = em.createQuery(sql);
		query.setParameter(1, filterId);
		try {
			return (String)query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

}
