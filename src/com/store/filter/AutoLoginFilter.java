package com.store.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.store.bean.UserBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;
import com.store.utils.CookUtils;

/**
 * Servlet Filter implementation class AutoLoginFilter
 */
public class AutoLoginFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			
			//先判断，现在session中还有没有那个userBean.
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			//还有，有效。
			if(userBean != null){
				chain.doFilter(request, response);
			}else{
				//代表session失效了。
				
				//2. 看cookie。
				
				//1. 来请求的时候，先从请求里面取出cookie , 但是cookie有很多的key-value
				Cookie[] cookies = request.getCookies();
				//2. 从一堆的cookie里面找出我们以前给浏览器发的那个cookie
				Cookie cookie = CookUtils.getCookieByName("auto_login", cookies);
				
				//第一次来
				if(cookie  == null){
					chain.doFilter(request, response);
				}else{
					//不是第一次。
					
					String value = cookie.getValue();
					String username = value.split("#itheima#")[0];
					String password = value.split("#itheima#")[1];
					//完成登录
					DatabaseService service = new DatabaseServiceImpl();
					userBean = service.getUserByUsernamePassword(username, password);
					//使用session存这个值到域中，方便下一次未过期前还可以用。
					request.getSession().setAttribute("userBean", userBean);
					
					chain.doFilter(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			chain.doFilter(req, response);
		}
	}

	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {}
}
