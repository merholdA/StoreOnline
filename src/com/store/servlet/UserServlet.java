package com.store.servlet;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.bean.UserBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;
import com.store.utils.CookUtils;
import com.store.utils.MailUtils;
import com.store.utils.MyBeanUtils;
import com.store.utils.PrintUtil;
import com.store.utils.UUIDUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 2L;

	/**
	 * 直接转向注册页面
	 * @param req
	 * @param resp
	 * @return
	 */
	private String registerUI(HttpServletRequest req, HttpServletResponse resp) {
		return "jsp/register.jsp";
	}
	
	/**
	 * 用户注册
	 * @param req
	 * @param resp
	 * @return
	 */
	private String userRegister(HttpServletRequest req, HttpServletResponse resp) {
		//PrintUtil.printServletRequestParameterMap(req);
		//1. 获取用户注册信息 并生成UID 和 Code
		UserBean user = new UserBean();
		MyBeanUtils.populate(user, req.getParameterMap());
		user.setUid(UUIDUtils.getCode());
		user.setCode(UUIDUtils.getCode());
		System.out.println(user.toString());
		//2. 写入数据库
		DatabaseService service = new DatabaseServiceImpl();
		if (service.addUser(user)) {
			req.setAttribute("msg", "用户注册成功,请激活!");
			//3. 注册成功发送邮件至用户邮箱xxx@store.com
			try {
				MailUtils.sendMail(user.getUsername()+"@store.com", user.getCode());
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} else {
			req.setAttribute("msg", "用户注册失败,请重新注册!");
		}
		return "jsp/info.jsp";
	}
	
	/**
	 * 激活用户
	 * @param req
	 * @param resp
	 * @return
	 */
	private String activateUser(HttpServletRequest req, HttpServletResponse resp) {
		String code = req.getParameter("code");
		DatabaseService service = new DatabaseServiceImpl();
		if (code!=null && code.length()>0) {
			int result = service.activateUser(code);
			if (result==0) {
				req.setAttribute("msg", "激活成功, 去<a href='"+getServletContext().getContextPath()+"/UserServlet?method=loginUI"+"'>登陆</a>");
			} else if (result==1) {
				req.setAttribute("msg", "用户不存在, 激活失败~");
			} else if (result==2) {
				req.setAttribute("msg", "已激活, 请再次确认用户信息");
			} else {
				req.setAttribute("msg", "激活失败...");
			}
		} else {
			req.setAttribute("msg", "激活失败...");
		}
		return "jsp/info.jsp";
	}
	
	/**
	 * 直接转向登陆页面
	 * @param req
	 * @param resp
	 * @return
	 */
	private String loginUI(HttpServletRequest req, HttpServletResponse resp) {
		Cookie ck = CookUtils.getCookieByName("username", req.getCookies());
		//从cookie获取username填入登录页面的用户名位置
		if (ck!=null) {
			String username = ck.getValue();
			if (username!=null && username.length()>0) {
				req.setAttribute("usernameLG", username);
			}
		}
		return "jsp/login.jsp";
	}
	
	/**
	 * 用户登录
	 * @param req
	 * @param resp
	 * @return
	 */
	private String userLogin(HttpServletRequest req, HttpServletResponse resp) {
		//PrintUtil.printServletRequestParameterMap(req);
		//1. 获取用户的登陆信息
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String autoLogin = req.getParameter("autoLogin");
		String recordUsername = req.getParameter("recordUsername");
		
		UserBean user = null;
		//2. 根据用户信息获取UserBean
		if (username!=null && username.length()>0 && password!=null && password.length()>0) {
			DatabaseService service = new DatabaseServiceImpl();
			user = service.getUserByUsernamePassword(username, password);
		}
		
		if (user==null) {//用户名或密码错误
			req.getSession().setAttribute("userBean", null);
			req.setAttribute("msg", "用户名或密码错误");
			return "jsp/login.jsp";
		} else {//用户存在 设置到Session 用于整个浏览过程---关闭所有相关网站---关闭浏览器
			req.getSession().setAttribute("userBean", user);
			//记住用户名
			if (recordUsername!=null && recordUsername.length()>0) {//使用cookie将username保存到客户浏览器
				Cookie ck = new Cookie("username", username);
				ck.setMaxAge(Integer.MAX_VALUE);
				ck.setPath(getServletContext().getContextPath());
				resp.addCookie(ck);
			} else {
				Cookie unCK = CookUtils.getCookieByName("username", req.getCookies());
				if (unCK!=null) {
					unCK.setMaxAge(0);
					resp.addCookie(unCK);
				}
			}
			//自动登录
			if (autoLogin!=null && autoLogin.length()>0) {
				//发送cookie给客户端
				Cookie cookie = new Cookie("auto_login", username+"#itheima#"+password);
				cookie.setMaxAge(60*60*24*7);//7天有效期
				cookie.setPath(getServletContext().getContextPath());
				resp.addCookie(cookie);
			} else {
				Cookie alCK = CookUtils.getCookieByName("auto_login", req.getCookies());
				if (alCK!=null) {
					alCK.setMaxAge(0);
					resp.addCookie(alCK);
				}
			}
			return "jsp/index.jsp";
		}
	}

	/**
	 * 用户退出
	 * @param req
	 * @param resp
	 * @return
	 */
	private String userLogout(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().invalidate();
		Cookie alCK = CookUtils.getCookieByName("auto_login", req.getCookies());
		if (alCK!=null) {
			alCK.setMaxAge(0);
			resp.addCookie(alCK);
		}
		return "jsp/index.jsp";
	}
}
