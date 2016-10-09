package com.harry.service.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.harry.entity.Menu;
import com.harry.entity.User;
import com.harry.jdbc.BaseDao;
import com.harry.service.MenuService;
import com.harry.util.DES;
@Service("menuService")
public class MenuServiceImpl extends BaseDao implements MenuService {

	public List<Menu> getMenuByParent(long userId, String parentId)
			throws Exception {
		try {
			String sql = "select m.* from T_MENUINFO m left join T_ROLE_MENU rm on m.MENUID=rm.MENUID left join T_ROLE_USER ru on ru.ROLEID=rm.ROLEID where ru.USERID=? and m.PARENTID=? order by m.PRIORITY";

			JdbcTemplate template = this.getJdbcTemplate();

			List<Menu> list = template.query(sql, new Object[] { userId,
					parentId }, new Menu());

			return list;
		} catch (RuntimeException exception) {
			throw exception;
		}
	}

}
