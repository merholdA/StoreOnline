package com.store.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String indexURL = null;//存放需要请求转发的地址
		//1. 获取需要执行的方法名
		String methodName = req.getParameter("method");
		//2. 反射执行对应方法
		if (methodName==null || methodName.length()<=0) {
			methodName = "defaultFunc";
		}
	
		Class clazz = this.getClass();
		try {
			Method method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			if (method!=null) {
				method.setAccessible(true);
				indexURL = (String) method.invoke(this, req, resp);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//3. 使用indexURL结果进行跳转
		if (null!=indexURL && indexURL.length()>0) {
			req.getRequestDispatcher(indexURL).forward(req, resp);
		}
	}
}
