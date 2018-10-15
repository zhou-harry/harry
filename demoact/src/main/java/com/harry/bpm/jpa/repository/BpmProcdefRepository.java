package com.harry.bpm.jpa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmProcdefService;

@Repository("bpmProcdefService")
public class BpmProcdefRepository extends BaseRepository<BpmProcdef> implements BpmProcdefService{

	public BpmProcdef findDefinitionIdByMatcher(String procType, Map<String, Object> vars) {
		String temp = "SELECT p.* FROM T_BPM_PROC_DEF p where KEY_ in(WITH Q AS(SELECT PROC_TYPE,DMS_KEY,MATCHER,PROC_KEY FROM T_BPM_PROC_MATCHER WHERE PROC_TYPE=?)select PROC_KEY from Q PIVOT(MAX(MATCHER)FOR DMS_KEY IN(%s))WHERE %s)";
		
		StringBuffer sb1=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		List<Object>params=new ArrayList<Object>();
		
		Set<String> ks = vars.keySet();
		for (String k : ks) {
			if (sb1.length()>0) {
				sb1.append(",");
				sb2.append(" and ");
			}
			sb1.append("'"+k+"'"+k);
			sb2.append(k+"=? ");
			params.add(vars.get(k));
		}
		String sql = String.format(temp, sb1.toString(), sb2.toString());
		
		Query query = em.createNativeQuery(sql,BpmProcdef.class);
		query.setParameter(1, procType);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i+2, params.get(i));
		}
		try {
			BpmProcdef defKey = (BpmProcdef) query.getSingleResult();
			return defKey;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public BpmProcdef findByKey(String key) {
		String sql = "select d from BpmProcdef d where key=?1";

		Query query = em.createQuery(sql, BpmProcdef.class);
		query.setParameter(1, key);
		try {
			BpmProcdef defKey = (BpmProcdef) query.getSingleResult();
			return defKey;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

}
