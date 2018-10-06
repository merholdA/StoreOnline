package com.store.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.bean.CartItemBean;
import com.store.bean.CartModeBean;
import com.store.bean.ProductBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;

/**
 * Servlet implementation class CartServlet
 * 购物车部分
 */
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private String addToCart(HttpServletRequest request, HttpServletResponse response) {
		CartModeBean cart = (CartModeBean) request.getSession().getAttribute("cart");
		if (cart==null) {
			cart = new CartModeBean();
			request.getSession().setAttribute("cart", cart);
		}
		String pid = request.getParameter("pid");
		if (pid!=null && pid.length()>0) {
			DatabaseService service = new DatabaseServiceImpl();
			ProductBean product = service.getProductByPid(pid);
			int quantity = Integer.valueOf(request.getParameter("quantity"));
			CartItemBean cartItem = new CartItemBean();
			cartItem.setProduct(product);
			cartItem.setCount(quantity);
			cart.addCartItem(cartItem);
			
			//重定向到 jsp/cart.jsp
			try {
				response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {//出错 是否转向他处 等 处理
			
		}
		return "";
	}
	
	private String delCartItem(HttpServletRequest request, HttpServletResponse response) {
		CartModeBean cart = (CartModeBean) request.getSession().getAttribute("cart");
		if (cart==null) {
			cart = new CartModeBean();
			request.getSession().setAttribute("cart", cart);
		}
		String pid = request.getParameter("pid");
		if (pid!=null && pid.length()>0) {
			cart.delCartItem(pid);
		}
		//重定向到 jsp/cart.jsp
		try {
			response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String clearCart(HttpServletRequest request, HttpServletResponse response) {
		CartModeBean cart = (CartModeBean) request.getSession().getAttribute("cart");
		if (cart==null) {
			cart = new CartModeBean();
			request.getSession().setAttribute("cart", cart);
		}
		cart.clearCartMode();
		//重定向到 jsp/cart.jsp
		try {
			response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
