package com.store.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.bean.ProductBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;

/**
 * Servlet implementation class IndexServlet
 */
public class IndexServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private String defaultFunc(HttpServletRequest request, HttpServletResponse response) {
		DatabaseService service = new DatabaseServiceImpl();
		//获取热门商品
		List<ProductBean> hotProducts = service.getHotProducts(9);
		//获取最新商品
		List<ProductBean> newestProducts = service.getNewestProducts(9);
		
		request.setAttribute("hotProducts", hotProducts);
		request.setAttribute("newestProducts", newestProducts);
		return "jsp/index.jsp";
	}

}
