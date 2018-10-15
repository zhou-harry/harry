package com.harry.fssc.service;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

@Component
public class SequenceService {

	private static Logger logger = LoggerFactory.getLogger(SequenceService.class);

	@Autowired
	private EntityManager em;

	@Transactional
	public String getNextVal(String seqName) {
		String sql = "select * from T_SYS_GENERATOR where KEY_=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, seqName);
		// Query 接口是 spring-data-jpa 的接口，而 SQLQuery 接口是 hibenate 的接口，这里的做法就是先转成 hibenate
		// 的查询接口对象，然后设置结果转换器
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		try {
			Object result = query.getSingleResult();
			if (null != result) {
				HashMap<String, Object> map = (HashMap<String, Object>) result;
				Object prefix = map.get("PREFIX_");
				Object len = map.get("LENGTH_");
				int lastNumber = this.lastNumber(seqName);
				if (lastNumber <= 0) {
					this.createSequence(seqName);
				}
				int val = this.nextVal(seqName);
				return prefix + String.format("%0" + len + "d", val);
			}
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
		return null;
	}

	private int lastNumber(String seqName) {
		String sql = "SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME=?";

		Query query = em.createNativeQuery(sql);
		query.setParameter(1, seqName);

		try {
			Object result = query.getSingleResult();
			if (null != result) {
				return ((BigDecimal) result).intValue();
			}
		} catch (NoResultException e) {
			return -1;
		} finally {
			em.close();
		}
		return 0;
	}

	@Modifying
	@Transactional
	public void createSequence(String seqName) {
		String sql = "create sequence DBUSER."+seqName+" increment by 1 start with 1 nocache nocycle";
		Query query = em.createNativeQuery(sql);
		
		query.executeUpdate();
		em.close();
	}

	private int nextVal(String seqName) {
		String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL ";

		Query query = em.createNativeQuery(sql);

		try {
			Object result = query.getSingleResult();
			if (null != result) {
				return ((BigDecimal) result).intValue();
			}
		} catch (NoResultException e) {
			return -1;
		} finally {
			em.close();
		}
		return 0;
	}
}
