package com.harry.bpm.jpa.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmDimension;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmDimensionService;

@Repository("bpmDimensionService")
public class BpmDimensionRepository extends BaseRepository<BpmDimension> implements BpmDimensionService {

	@Override
	@Transactional
	public void delete(BpmDimension dimension) {

		Query query = em.createQuery("DELETE FROM BpmDimension WHERE key=?1");
		
		query.setParameter(1, dimension.getKey());
		
		long rows = query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public List<BpmDimension> findByProcType(String procType) {

		String sql = "select d.* from T_BPM_DIMENSION d left join T_BPM_PROC_DMS td  on d.KEY_=td.DMS_KEY where td.PROC_TYPE=?";

		Query query = em.createNativeQuery(sql, BpmDimension.class);
		query.setParameter(1, procType);
		try {
			List<BpmDimension> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
}
