package com.harry.fssc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Component;

import com.harry.fssc.model.SideContent;
import com.harry.fssc.model.User;
import com.harry.fssc.repository.MenuRepository;

@Component
public class MenuService {
	private static Logger logger = LoggerFactory.getLogger(MenuService.class);
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private MenuRepository repository;
	
//	@Cacheable(value = "sideContent", keyGenerator = "harryKeyGenerator",unless="#result == null")
	public List<SideContent> initSideContent(String userId,String parentKey) {

		List<SideContent> list = repository.findByAuth(userId,parentKey);
		
		if (list instanceof RandomAccess) {
			for (int i = 0; i < list.size(); i++) {
				SideContent side = list.get(i);
				side.setItems(repository.findByAuth(userId,side.getKey()));
			}
		}

		return list;
	}
	
	public List<SideContent> findByForm(boolean isform) {

		List<SideContent> list = repository.findByForm(isform);
		
		return list;
	}
	
	public SideContent findByKey(String key) {

		return repository.findByKey(key);
		
	}
	
	public List<SideContent> monitorSystem() {

		List<SideContent> list = repository.findByParentKeyOrderByIndex("systemManager");
		
		if (list instanceof RandomAccess) {
			for (int i = 0; i < list.size(); i++) {
				SideContent side = list.get(i);
				side.setItems(repository.findByParentKeyOrderByIndex(side.getKey()));
			}
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> findWithRole(String roleId,String parentKey) {
		String sql = "select m.URL_KEY as key,m.TITLE,m.ICON,nvl2(r.ROLEID,1,0)valid from T_SIDE_CONTENT m " + 
				"left join T_ROLE_MENU rm on m.URL_KEY=rm.MENUID  AND rm.ROLEID=? " + 
				"left join T_ROLE r on r.ROLEID=rm.ROLEID AND r.STATUS=1 " + 
				"where PARENT_KEY=? order by INDEX_";
		
		Query query = em.createNativeQuery(sql);

		query.setParameter(1, roleId);
		query.setParameter(2, parentKey);

		// Query 接口是 spring-data-jpa 的接口，而 SQLQuery 接口是 hibenate 的接口，这里的做法就是先转成 hibenate 的查询接口对象，然后设置结果转换器
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		List<HashMap<String, Object>> list = query.getResultList();
		em.close();
		
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map = list.get(i);
			BigDecimal v = (BigDecimal)map.get("VALID");
			map.put("VALID", new BigDecimal(1).compareTo(v)==0);
		}
		
		return list;
	}
	/**
	 * 检查传入的菜单key是否存在子菜单
	 * @param key
	 * @return
	 */
	public boolean existsByParentKey(String key) {
		//The ExampleMatcher is immutable and can be static I think
		ExampleMatcher NAME_MATCHER = ExampleMatcher.matching()
		            .withMatcher("parentKey", GenericPropertyMatchers.ignoreCase());
		Example<SideContent> example = Example.<SideContent>of(new SideContent(key), NAME_MATCHER);
		return repository.exists(example);
	}
}
