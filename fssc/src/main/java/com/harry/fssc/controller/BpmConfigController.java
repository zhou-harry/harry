package com.harry.fssc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harry.bpm.bean.BpmDimension;
import com.harry.bpm.bean.BpmFilter;
import com.harry.bpm.bean.BpmFilterMatcher;
import com.harry.bpm.bean.BpmMatcher;
import com.harry.bpm.bean.BpmOwner;
import com.harry.bpm.bean.BpmProcdef;
import com.harry.bpm.bean.BpmProcdms;
import com.harry.bpm.bean.BpmProctype;
import com.harry.bpm.bean.BpmRole;
import com.harry.bpm.bean.BpmTask;
import com.harry.bpm.bean.BpmTaskRole;
import com.harry.bpm.bean.pk.BpmProcdmsPK;
import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.BpmService;
import com.harry.fssc.service.SequenceService;
import com.harry.fssc.util.Const;

@RestController
@RequestMapping("bpmConfig")
public class BpmConfigController {
	
	@Autowired
	private BpmService service;
	@Autowired
	private SequenceService sequence;

	@RequestMapping(value = "findDimension", method = RequestMethod.POST)
	public ResponseData findDimension(HttpServletRequest request, HttpServletResponse response) {

		List<BpmDimension> list = service.findDimension();

		return new ResponseData(ResultCode.SUCCESS, list);

	}
	
	@RequestMapping(value = "findProcDms", method = RequestMethod.POST)
	public ResponseData findProcDms(HttpServletRequest request, HttpServletResponse response,String procType) {

		List<BpmDimension> list = service.findProcDms(procType);

		return new ResponseData(ResultCode.SUCCESS, list);

	}
	
	@RequestMapping(value = "findPMatcher", method = RequestMethod.POST)
	public ResponseData findProcMatcher(HttpServletRequest request, HttpServletResponse response,String type) {
	
		List<Map<String,Object>> list = service.findProcMatcher(type);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findProcdef", method = RequestMethod.POST)
	public ResponseData findProcdef(HttpServletRequest request, HttpServletResponse response) {
	
		List<BpmProcdef> list = service.findProcdef();
		
		if (null!=list) {
			for (int i = 0; i < list.size(); i++) {
				BpmProcdef p = list.get(i);
				p.setCountTasks(service.countTasks(p.getKey()));
			}
		}
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findFilter", method = RequestMethod.POST)
	public ResponseData findFilter(HttpServletRequest request, HttpServletResponse response) {
	
		List<BpmFilter> list = service.findFilter();
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findFilterByKey", method = RequestMethod.POST)
	public ResponseData findFilterByKey(HttpServletRequest request, HttpServletResponse response,String key) {
	
		Map<String, Object> list = service.findFilterByKey(key);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findForRelation", method = RequestMethod.POST)
	public ResponseData findForRelation(HttpServletRequest request, HttpServletResponse response,String key) {
	
		List<BpmFilter> list = service.findForRelation(key);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findFilterMatcherByKey", method = RequestMethod.POST)
	public ResponseData findFilterMatcherByKey(HttpServletRequest request, HttpServletResponse response,String key) {
	
		List<BpmFilterMatcher> list = service.findByMatcherFilterKey(key);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findRoles", method = RequestMethod.POST)
	public ResponseData findRoles(HttpServletRequest request, HttpServletResponse response) {
	
		List<BpmRole> list = service.findRoles();
		
		if (null!=list) {
			for (int i = 0; i < list.size(); i++) {
				BpmRole role = list.get(i);
				role.setCountOwners(service.countOwnerByRole(role.getRoleId()));
			}
		}
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findOwnersByRole", method = RequestMethod.POST)
	public ResponseData findOwnersByRole(HttpServletRequest request, HttpServletResponse response,String roleid) {
	
		List<BpmOwner> list = service.findOwnersByRole(roleid);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findTasks", method = RequestMethod.POST)
	public ResponseData findTasks(HttpServletRequest request, HttpServletResponse response,String key) {
	
		List<BpmTask> list = service.findTasks(key);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}
	
	@RequestMapping(value = "findRoleByTask", method = RequestMethod.POST)
	public ResponseData findRoleByTask(HttpServletRequest request, HttpServletResponse response,String key) {
	
		List<BpmTaskRole> list = service.findRoleByTask(key);
	
		return new ResponseData(ResultCode.SUCCESS, list);
	
	}

	@RequestMapping(value = "saveDimension", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.BPM_DIMENSION_SAVE)
	public ResponseData saveDimension(HttpServletRequest request, HttpServletResponse response, @RequestBody BpmDimension t) {
	
		service.saveDimension(t);
	
		return new ResponseData(ResultCode.SUCCESS, "OK");
	
	}

	@RequestMapping(value = "saveProcDms", method = RequestMethod.POST)
	public ResponseData saveProcDms(HttpServletRequest request, HttpServletResponse response,@RequestBody List<BpmProcdms>list) {

		if (null!=list) {
			for (int i = 0; i < list.size(); i++) {
				BpmProcdms pdm = list.get(i);
				pdm.setIdkey(new BpmProcdmsPK(pdm.getTypeName(), pdm.getDmsDesc()));
			}
			service.saveProcDms(list);
			
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "savePMatcher", method = RequestMethod.POST)
	public ResponseData savePMatcher(HttpServletRequest request, HttpServletResponse response,@RequestBody List<BpmMatcher> list) {
	
		if (null!=list) {
			service.saveProcMatcher(list);
			
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveProcdef", method = RequestMethod.POST)
	public ResponseData saveProcdef(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmProcdef t) {
	
		if (null!=t) {
			service.saveProcdef(t);
			
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveFilter", method = RequestMethod.POST)
	public ResponseData saveFilter(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmFilter t) {
	
		if (null!=t) {
			String fid=t.getFilterId();
			fid=("undefined".equals(fid)||"null".equals(fid))?null:fid;
			if (null==fid) {
				t.setFilterId(sequence.getNextVal(Const.SEQ_FILTERID));
			}
			if (t.getType()!=3) {
				t.setMaster(null);
				t.setSlave(null);
				t.setRelation(null);
			}else if (t.getType()!=2) {
				service.deleteFilterMatcher(t.getFilterId());
			}
			service.saveFilter(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveFilterMatcher", method = RequestMethod.POST)
	public ResponseData saveFilterMatcher(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmFilterMatcher t) {
	
		if (null!=t) {
			if (t.getFilterId()==null) {
				return new ResponseData(ResultCode.FAILED, "数据无效");
			}
			service.saveFilterMatcher(t);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}

	@RequestMapping(value = "saveRole", method = RequestMethod.POST)
	public ResponseData saveRole(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmRole t) {
	
		if (null!=t) {
			if (t.getRoleId()==null) {
				t.setRoleId(sequence.getNextVal(Const.SEQ_BPM_ROLEID));
			}
			service.saveRole(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	@RequestMapping(value = "saveOwner", method = RequestMethod.POST)
	public ResponseData saveOwner(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmOwner t) {
	
		if (null!=t) {
			if (t.getRoleId()==null) {
				return new ResponseData(ResultCode.FAILED, "数据无效");
			}
			service.saveOwner(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveTask", method = RequestMethod.POST)
	public ResponseData saveTask(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmTask t) {
	
		if (null!=t) {
			if (t.getTaskId()==null) {
				t.setTaskId(sequence.getNextVal(Const.SEQ_BPM_TASKID));
			}
			if (null==t.getIsSelect()) {
				t.setIsSelect(false);
			}
			if (null==t.getPending()) {
				t.setPending(false);
			}
			service.saveTask(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveTaskRole", method = RequestMethod.POST)
	public ResponseData saveTaskRole(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmTaskRole t) {
	
		if (null!=t) {
			service.saveTaskRole(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}
	
	@RequestMapping(value = "saveProctype", method = RequestMethod.POST)
	public ResponseData saveProcType(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmProctype t) {
	
		if (null!=t) {
			service.saveProctype(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}
	
	}

	@RequestMapping(value = "delDimension", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.BPM_DIMENSION_SAVE)
	public ResponseData deleteDimension(HttpServletRequest request, HttpServletResponse response, @RequestBody BpmDimension t) {
	
		service.deleteDimension(t);
	
		return new ResponseData(ResultCode.SUCCESS, "OK");
	
	}

	@RequestMapping(value = "deleteProcDms", method = RequestMethod.POST)
	public ResponseData deleteProcDms(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmProcdms procdms) {

		if (null!=procdms) {
			procdms.setIdkey(new BpmProcdmsPK(procdms.getTypeName(), procdms.getDmsDesc()));
			service.deleteProcDms(procdms);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deletePMatcher", method = RequestMethod.POST)
	public ResponseData deletePMatcher(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmMatcher form) {

		if (null!=form) {
			service.deleteProcMatcher(form);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteProcdef", method = RequestMethod.POST)
	public ResponseData deleteProcdef(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmProcdef t) {

		if (null!=t) {
			service.deleteProcdef(t);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteFilter", method = RequestMethod.POST)
	public ResponseData deleteFilter(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmFilter t) {

		if (null!=t) {
			service.deleteFilter(t);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteFilterMatcher", method = RequestMethod.POST)
	public ResponseData deleteFilterMatcher(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmFilterMatcher t) {

		if (null!=t) {
			service.deleteFilterMatcher(t);
			return new ResponseData(ResultCode.SUCCESS, "OK");
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteRole", method = RequestMethod.POST)
	public ResponseData deleteRole(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmRole t) {

		if (null!=t) {
			service.deleteRole(t);
			service.deleteOwnerByRole(t.getRoleId());
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteOwner", method = RequestMethod.POST)
	public ResponseData deleteOwner(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmOwner t) {

		if (null!=t) {
			service.deleteOwner(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteTask", method = RequestMethod.POST)
	public ResponseData deleteTask(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmTask t) {

		if (null!=t) {
			service.deleteTask(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
	@RequestMapping(value = "deleteTaskRole", method = RequestMethod.POST)
	public ResponseData deleteTaskRole(HttpServletRequest request, HttpServletResponse response,@RequestBody BpmTaskRole t) {

		if (null!=t) {
			service.deleteTaskRole(t);
			return new ResponseData(ResultCode.SUCCESS, t);
		}else {
			return new ResponseData(ResultCode.FAILED, "数据为空");
		}

	}
	
}
