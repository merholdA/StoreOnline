package com.store.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.bean.CategoryBean;
import com.store.bean.ProductBean;
import com.store.bean.ProductPagingCgyBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private String getProductDetail(HttpServletRequest request, HttpServletResponse response) {
		String pid = request.getParameter("pid");
		DatabaseService service = new DatabaseServiceImpl();
		ProductBean product = service.getProductByPid(pid);
		request.setAttribute("product", product);
		if (product!=null) {
			CategoryBean category = service.getCategoryById(product.getCid());
			request.setAttribute("category", category);
		}
		return "jsp/product_info.jsp";
	}
	
	/**
	 * 根据分类进行分页显示
	 * @param request
	 * @param response
	 * @return
	 */
	private String getProductByCategoryWithPaging(HttpServletRequest request, HttpServletResponse response) {
		int pageSize = 12;//每页大小为12项
		String cid = request.getParameter("cid");//类型
		int pageIndex = Integer.valueOf(request.getParameter("pageIndex"));
		DatabaseService service = new DatabaseServiceImpl();
		ProductPagingCgyBean ppcBean = service.getProtByCgyWithPaging(cid, pageSize, pageIndex);
		request.setAttribute("page", ppcBean);
		return "jsp/product_list.jsp";
	}
}
