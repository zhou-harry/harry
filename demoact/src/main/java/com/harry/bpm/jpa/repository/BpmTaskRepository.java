package com.harry.bpm.jpa.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.enums.TaskState;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmTaskService;

@Repository("bpmTaskService")
public class BpmTaskRepository extends BaseRepository<BpmTask> implements BpmTaskService {

	public BpmTask findNextTask(String definitionId, String taskId) {
		String sql = null;
		if (null == taskId) {
			sql = "select * from(select rownum,t.* from T_BPM_TASK_DEF t where PROC_DEF_ID=? and STATUS=? order by STEP)where rownum=1";
		} else {
			sql = "select * from(select rownum,t.* from T_BPM_TASK_DEF t where STEP>(select STEP from T_BPM_TASK_DEF sb1 where sb1.PROC_DEF_ID=PROC_DEF_ID and sb1.TASK_DEF_ID=?)and PROC_DEF_ID=? and STATUS=? order by STEP)where rownum=1";
		}

		Query query = em.createNativeQuery(sql, BpmTask.class);
		if (null == taskId) {
			query.setParameter(1, definitionId);
			query.setParameter(2, TaskState.ACTIVE.getKey());
		} else {
			query.setParameter(1, taskId);
			query.setParameter(2, definitionId);
			query.setParameter(3, TaskState.ACTIVE.getKey());
		}

		try {
			Object result = query.getSingleResult();
			if (null!=result) {
				return (BpmTask)result;
			}
		} catch (NoResultException e) {
			return null;
		}finally {
			em.close();
		}
		return null;
	}

	public List<BpmTask> findTasks(String definitionId) {
		String sql = "select * from T_BPM_TASK_DEF t where t.PROC_DEF_ID=? order by STEP";

		Query query = em.createNativeQuery(sql, BpmTask.class);
		query.setParameter(1, definitionId);
		try {
			List<BpmTask> list = query.getResultList();
			return list;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public Integer countTasks(String definitionId) {
		String sql = "select count(*) from T_BPM_TASK_DEF t where t.PROC_DEF_ID=?";

		Query query = em.createNativeQuery(sql);

		query.setParameter(1, definitionId);

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

	

}
