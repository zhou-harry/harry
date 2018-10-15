package com.harry.bpm.jpa.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmFilter;
import com.harry.bpm.bean.BpmFilterMatcher;
import com.harry.bpm.jpa.service.BpmFilterMatcherService;

@Repository("bpmFilterMatcherService")
public class BpmFilterMatcherRepository extends BaseRepository<BpmFilterMatcher> implements BpmFilterMatcherService {

	public List<BpmFilterMatcher> findByFilterKey(String key) {
		String sql = "SELECT * FROM T_BPM_FILTER_MATCHER f where f.FILTER_ID =?";
		Query query = em.createNativeQuery(sql, BpmFilterMatcher.class);
		query.setParameter(1, key);
		try {
			List<BpmFilterMatcher> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
	@Transactional
	public void deleteByFilterKey(String key) {
		
		Query query = em.createQuery("DELETE FROM BpmFilterMatcher WHERE filterId=?1");

		query.setParameter(1, key);

		query.executeUpdate();
	}

}
