package com.harry.util.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.harry.entity.User;
import com.harry.util.Const;
import com.harry.util.session.SessionProvider;

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private SessionProvider session;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			User user = (User)session.getAttribute(request, Const.SESSION_USER);
			if(user!=null){
				path = path.substring(1, path.length());
				boolean b = true;//访问权限校验
				if(!b){
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}
				return b;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
			}
		}
	}
}
