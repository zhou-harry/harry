package com.harry.fssc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.harry.fssc.model.SideContent;

public interface MenuRepository extends JpaRepository<SideContent, Long> {

	List<SideContent>findByParentKeyOrderByIndex(String parentKey);
	
	@Query(value = "select distinct m.* from T_SIDE_CONTENT m " + 
			"left join T_ROLE_MENU r on r.MENUID=m.URL_KEY " + 
			"left join T_ROLE_USER u on u.ROLEID=r.ROLEID " + 
			"where u.USERID=?1 and m.PARENT_KEY = ?2 and m.VALID=1 order by INDEX_", nativeQuery = true)
	List<SideContent>findByAuth(String userId,String parentKey);
	
	List<SideContent>findByForm(boolean isform);
	
	SideContent findByKey(String key);
	
}
