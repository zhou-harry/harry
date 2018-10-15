package com.harry.bpm.jpa.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmOwner;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmOwnerService;

@Repository("bpmOwnerService")
public class BpmOwnerRepository extends BaseRepository<BpmOwner> implements BpmOwnerService{

	public List<String> findOwnerIdByTask(String definitionId, String taskId) {
		String sql = "select OWNER_ID from T_BPM_ROLE_OWNER ro left join T_BPM_TASK_ROLE tr on ro.ROLEID=tr.ROLEID where PROC_DEF_ID=? and TASK_DEF_ID=?";

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

	public List<String> findOwnerIdByRole(String roleId) {
		String sql = "select OWNER_ID from T_BPM_ROLE_OWNER where ROLEID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, roleId);
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
	
	public List<BpmOwner> findOwnerByRole(String roleid) {
		String sql = "select o.*,u.NAME,f.NAME_ from T_BPM_ROLE_OWNER o left join T_USER u on o.OWNER_ID=u.USER_ID left join T_BPM_FILTER f on o.FILTER_ID=f.FILTER_ID where o.ROLEID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, roleid);

		List<BpmOwner>result=new ArrayList<BpmOwner>();
		try {
			List list = query.getResultList();
			if (null!=list) {
				for (int i = 0; i < list.size(); i++) {
					Object[] row = (Object[])list.get(i);
					BpmOwner owner = new BpmOwner();
					owner.setId(((BigDecimal)row[0]).longValue());
					owner.setRoleId((String)row[1]);
					owner.setFilterId((String)row[2]);
					owner.setOwnerId((String)row[3]);
					owner.setOwnerName((String)row[4]);
					owner.setFilterName((String)row[5]);
					result.add(owner);
				}
			}
		} catch (NoResultException e) {
			return result;
		} finally {
			em.close();
		}
		return result;
	}
	
	public Integer countOwnerByRole(String roleid) {
		String sql = "select count(*) from T_BPM_ROLE_OWNER o where o.ROLEID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, roleid);

		try {
			Object obj = query.getSingleResult();
			if (null!=obj) {
				return ((BigDecimal)obj).intValue();
			}
		} catch (NoResultException e) {
			return 0;
		} finally {
			em.close();
		}
		return 0;
	}
	@Transactional
	public void deleteByRole(String roleid) {
		Query query = em.createQuery("DELETE FROM BpmOwner WHERE roleId=?1");

		query.setParameter(1, roleid);

		query.executeUpdate();
	}

}
