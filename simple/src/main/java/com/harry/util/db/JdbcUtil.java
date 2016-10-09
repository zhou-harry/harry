package com.harry.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class JdbcUtil {
	private static Logger logger = Logger.getLogger(JdbcUtil.class);
	
	protected static Long getNextVal(String seqName, JdbcTemplate template)
			throws RuntimeException {
		Long id = null;
		boolean result = true;
		try {
			String sequence = checkSequence(seqName, template);
			if (null == sequence) {
				result = createSequence(seqName, template);
			}
			if (result) {
				String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL ";
				id = template.queryForObject(sql, Long.class);
			}
		} catch (Exception e) {
			logger.error("获取sequence异常:" + e.getMessage());
		}
		return id;

	}
	
	private static String checkSequence(String seqName, JdbcTemplate template) {
		String sql = "SELECT SEQUENCE_NAME FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER='"
				+ Propertise.SCHEMA + "'  AND SEQUENCE_NAME=?";
		String sequenceName = null;
		try {
			sequenceName = template.queryForObject(sql, String.class, seqName);
		} catch (RuntimeException e) {
		}
		return sequenceName;
	}
	
	private static Boolean createSequence(String seqName, JdbcTemplate template) {
		String sql = "CREATE SEQUENCE "
				+ seqName
				+ " MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER  NOCYCLE ";
		try {
			template.execute(sql);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}
}
