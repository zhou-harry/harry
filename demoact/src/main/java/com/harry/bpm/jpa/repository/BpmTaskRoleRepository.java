package com.harry.bpm.jpa.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmTaskRole;
import com.harry.bpm.jpa.service.BpmTaskRoleService;
@Repository("bpmTaskRoleService")
public class BpmTaskRoleRepository extends BaseRepository<BpmTaskRole> implements BpmTaskRoleService {


	public List<BpmTaskRole> findByTask(String taskId) {
		String sql = "select tr.*,f.NAME_ fname,r.NAME_ rname from T_BPM_TASK_ROLE tr left join T_BPM_ROLE r on r.ROLEID=tr.ROLEID left join T_BPM_FILTER f on tr.FILTER_ID=f.FILTER_ID where tr.TASK_DEF_ID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, taskId);

		List<BpmTaskRole>result=new ArrayList<BpmTaskRole>();
		try {
			List list = query.getResultList();
			if (null!=list) {
				for (int i = 0; i < list.size(); i++) {
					Object[] row = (Object[])list.get(i);
					BpmTaskRole obj = new BpmTaskRole();
					obj.setId(((BigDecimal)row[0]).longValue());
					obj.setDefinitionId((String)row[1]);
					obj.setTaskId((String)row[2]);
					obj.setRoleId((String)row[3]);
					obj.setFilterId((String)row[4]);
					obj.setFilterName((String)row[5]);
					obj.setRoleName((String)row[6]);
					result.add(obj);
				}
			}
		} catch (NoResultException e) {
			return result;
		} finally {
			em.close();
		}
		return result;
	}

}
