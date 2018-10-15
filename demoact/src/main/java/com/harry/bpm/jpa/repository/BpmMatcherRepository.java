package com.harry.bpm.jpa.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmMatcher;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmMatcherService;

@Repository("bpmMatcherService")
public class BpmMatcherRepository extends BaseRepository<BpmMatcher> implements BpmMatcherService {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findProcMatcher(String procType) {
		String sql = "with q as(select distinct PROC_TYPE,PROC_KEY from T_BPM_PROC_MATCHER where PROC_TYPE=?)select PROC_TYPE,m.NAME_ TYPE_NAME,PROC_KEY,p.NAME_ DEF_NAME from q left join T_BPM_PROC_TYPE m on q.PROC_TYPE=m.TYPE_ left join T_BPM_PROC_DEF p on q.PROC_KEY=p.KEY_";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, procType);
		List<Map<String, Object>> list;
		try {
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.getResultList();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = list.get(i);
				String procKey = (String) row.get("PROC_KEY");
				row.put("items", this.findTypeDms(procType, procKey));
			}
		} catch (NoResultException e) {
			throw new BpmException(BpmMessage.E0000, e);
		} finally {
			em.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTypeDms(String procType, String procKey) {
		String sql = "with q as(select DMS_KEY,MATCHER from T_BPM_PROC_MATCHER where PROC_TYPE=? and PROC_KEY=?)select DMS_KEY,NAME_,MATCHER from q left join T_BPM_DIMENSION d on d.KEY_=q.DMS_KEY";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, procType);
		query.setParameter(2, procKey);
		List<Map<String, Object>> list;
		try {
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.getResultList();
		} catch (NoResultException e) {
			throw new BpmException(BpmMessage.E0000, e);
		} finally {
			em.close();
		}
		return list;
	}

	@Override
	@Transactional
	public void delete(BpmMatcher t) {
		if (null != t) {
			Query query = em.createQuery("DELETE FROM BpmMatcher WHERE procType=?1 and procKey=?2");

			query.setParameter(1, t.getProcType());
			query.setParameter(2, t.getProcKey());

			long rows = query.executeUpdate();
		}
	}

}
