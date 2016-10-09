package com.harry.util.db;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class SystemCodeUtil extends JdbcUtil {
	
	private static Logger logger = Logger.getLogger(SystemCodeUtil.class);

	public static String getRoleId(JdbcTemplate template)
			throws RuntimeException {
		String bh = null;
		try {
			if (null == bh) {
				Long no = getNextVal("T_ROLE_SEQ", template);
				String id = String.format("%09d", no);
				bh = "R" + id;
			}
		} catch (RuntimeException e) {
			logger.error("取角色编号异常:" + bh);
			e.printStackTrace();
		}
		return bh;
	}
	
	
}
