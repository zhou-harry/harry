package com.harry.fssc.session;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.shiro.mgt.SecurityManager;

import com.harry.fssc.model.User;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.util.Const;
@Component
public class SessionUtil {
	
	@Autowired
    private RedisSessionDAO sessionDAO;
	
	private SecurityManager securityManager;
	
	public SessionUtil(SecurityManager securityManager){
		this.securityManager=securityManager;
	}
	/**
	 * 验证是否登陆
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) {
		boolean status = false;
		String sessionID = request.getSession().getId();
		SessionKey key = new WebSessionKey(sessionID, request, response);
		try {
			Session se = securityManager.getSession(key);
			Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
			if (obj != null) {
				status = (Boolean) obj;
			}else {
				se.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response,Serializable sessionID) {
		boolean status = false;
		SessionKey key = new WebSessionKey(sessionID, request, response);
		try {
			Session se = securityManager.getSession(key);
			Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
			if (obj != null) {
				status = (Boolean) obj;
			}else {
				se.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	/**
	 * 获取用户登录信息
	 * @param request
	 * @param response
	 * @return
	 */
	public User getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		User user=null;
		String sessionID = request.getSession().getId();
		SessionKey key = new WebSessionKey(sessionID, request, response);
		try {
			Session se = securityManager.getSession(key);
			Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (null!=obj) {
				SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
				user=(User) coll.getPrimaryPrincipal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 在线会话清单
	 * @param model
	 * @return
	 */
	public Collection<Session> sessionList() {
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        return sessions;
    }
	/**
	 * 强制注销
	 * @param sessionId
	 */
	public ResultCode logout(String sessionId) {
		Subject subject = SecurityUtils.getSubject();
		try {
			Session session = subject.getSession();
			if (session.getId().equals(sessionId)) {
				return ResultCode.CURRENT_SESSION_OUT;
			}else {
				sessionDAO.delete(sessionDAO.readSession(sessionId));
				return ResultCode.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultCode.FAILED;
	}
	/**
	 * 同一个账号只能有一个会话
	 * @param sessionId
	 * @return
	 */
	public void kickout(Subject subject) {
		try {
			Session cs = subject.getSession();
			User cu = (User)subject.getPrincipal();
			Collection<Session> sessions = sessionDAO.getActiveSessions();
			for (Session session : sessions) {
				if (!cs.getId().equals(session.getId())) {
					Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
					if (null != obj) {
						User user = (User) ((SimplePrincipalCollection) obj).getPrimaryPrincipal();
						if (cu.equals(user)) {
							sessionDAO.delete(session);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
