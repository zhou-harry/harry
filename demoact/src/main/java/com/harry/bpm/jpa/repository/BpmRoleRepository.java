package com.harry.bpm.jpa.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmRole;
import com.harry.bpm.enums.RoleType;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmRoleService;

@Repository("bpmRoleService")
public class BpmRoleRepository extends BaseRepository<BpmRole> implements BpmRoleService {


	public List<String> findRoleIdByTask(String definitionId, String taskId) {
		
		String sql = "select ROLEID from T_BPM_TASK_ROLE where PROC_DEF_ID=? and TASK_DEF_ID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, definitionId);
		query.setParameter(2, taskId);

		try {
			List list = query.getResultList();
			if (null!=list) {
				return (List<String>)list;
			}
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
		return null;
	}

	public List<String> findCopyRoleIdByTask(String definitionId, String taskId) {
		String sql = "select r.ROLEID from T_BPM_ROLE r left join T_BPM_TASK_ROLE t on r.ROLEID=t.ROLEID where PROC_DEF_ID=? and TASK_DEF_ID=? and r.TYPE_=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, definitionId);
		query.setParameter(2, taskId);
		query.setParameter(3, RoleType.COPY.getKey().intValue());
		try {
			List list = query.getResultList();
			if (null!=list) {
				return (List<String>)list;
			}
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
		return null;
	}

}
