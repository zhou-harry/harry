package com.harry.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.harry.entity.Role;
import com.harry.entity.Tree;
import com.harry.jdbc.BaseDao;
import com.harry.service.RoleService;
import com.harry.util.Result;
import com.harry.util.Tools;
import com.harry.util.db.SystemCodeUtil;

@Service("roleService")
public class RoleServiceImpl extends BaseDao implements RoleService {

	private static final String ROOT_ROLE="R00000";
	
	public List<Role> queryRole(Role input) {
		try {
			List<Object> para = new ArrayList<Object>();
			String sql = "select * from T_ROLE where 1=1 ";
			if (null != input) {
				if (Tools.notEmpty(input.getRoleid())) {
					sql += " AND ROLEID= ? ";
					para.add(input.getRoleid());
				}
				if (Tools.notEmpty(input.getRolename())) {
					sql += "AND ROLENAME LIKE ?";
					para.add("%" + input.getRolename() + "%");
				}
				if (Tools.notEmpty(input.getParentid())) {
					sql += " AND PARENT_RID= ? ";
					para.add(input.getParentid());
				}
				if (Tools.notEmpty(input.getStatus())) {
					sql += " AND STATUS= ? ";
					para.add(input.getStatus());
				}
			}
			JdbcTemplate template = this.getJdbcTemplate();

			List<Role> list = template.query(sql, para.toArray(), new Role());

			return list;
		} catch (RuntimeException exception) {
			throw exception;
		}
	}
	
	public List<Role> queryRole(String input) {
		try {
			List<Object> para = new ArrayList<Object>();
			String sql = "select * from T_ROLE where 1=1 ";
			if (null != input) {
				if (Tools.notEmpty(input)) {
					if (input.equals("生效")) {
						sql += " AND STATUS= 1 ";
					}else if (input.equals("失效")) {
						sql += " AND STATUS= 0 ";
					}else{
						sql += " AND (ROLEID like ? OR ROLENAME LIKE ? OR PARENT_RID LIKE ?) ";
						para.add("%" + input + "%");
						para.add("%" + input + "%");
						para.add("%" + input + "%");
					}
				}
			}
			JdbcTemplate template = this.getJdbcTemplate();

			List<Role> list = template.query(sql, para.toArray(), new Role());

			return list;
		} catch (RuntimeException exception) {
			throw exception;
		}
	}

	public Result saveRole(Role input) throws RuntimeException {
		JdbcTemplate execute = this.getJdbcTemplate();
		if (null == input.getRoleid()) {
			String roleid = SystemCodeUtil.getRoleId(execute);
			input.setRoleid(roleid);
		}

		String sql = "MERGE INTO T_ROLE t USING (SELECT ? as ROLEID,? as ROLENAME,? as PARENT_RID,? as STATUS FROM DUAL) r on (t.ROLEID=r.ROLEID) "
				+ "WHEN MATCHED THEN UPDATE SET t.ROLENAME=r.ROLENAME,t.PARENT_RID=r.PARENT_RID,STATUS=r.STATUS "
				+ "WHEN NOT MATCHED THEN INSERT (ROLEID, ROLENAME,PARENT_RID,STATUS)VALUES(r.ROLEID,r.ROLENAME,r.PARENT_RID,r.STATUS)";

		List<Object> para = new ArrayList<Object>();
		para.add(input.getRoleid());
		para.add(input.getRolename());
		para.add(input.getParentid());
		para.add(input.getStatus());

		int count = execute.update(sql, para.toArray());
		return new Result(true);
	}

	public List<Tree> getRoleTree() {
		try {
			List<Object> para = new ArrayList<Object>();
			para.add(ROOT_ROLE);
			String sql = "SELECT LEVEL,CONNECT_BY_ISLEAF AS isleaf,ROLEID,ROLENAME,PARENT_RID  "
					+ "FROM T_ROLE START WITH ROLEID = ? CONNECT BY PRIOR ROLEID = PARENT_RID";
			JdbcTemplate template = this.getJdbcTemplate();

			List<Tree> list = template.query(sql, para.toArray(), new Tree());

			return list;
		} catch (RuntimeException exception) {
			throw exception;
		}
	}

}
