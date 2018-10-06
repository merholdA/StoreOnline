package com.store.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.bean.CartItemBean;
import com.store.bean.CartModeBean;
import com.store.bean.OrderBean;
import com.store.bean.OrderItemBean;
import com.store.bean.OrderPaging;
import com.store.bean.UserBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;
import com.store.utils.JDBCUtil;
import com.store.utils.PaymentUtil;
import com.store.utils.UUIDUtils;

/**
 * Servlet implementation class OrderServlet
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private String submitOrder(HttpServletRequest request, HttpServletResponse response) {
		//检查是否已登录
		UserBean user = (UserBean) request.getSession().getAttribute("userBean");
		if (user==null) {//没有登录 请先登录
			request.setAttribute("msg", "请先登录~");
			return "jsp/info.jsp";
		}
		String address = request.getParameter("address");
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		//将购物车转换为订单
		CartModeBean cartMode = (CartModeBean) request.getSession().getAttribute("cart");
		OrderBean order = new OrderBean();
		order.setOid(UUIDUtils.getId());
		order.setOrdertime(new Date());
		order.setTotal(cartMode.getTotalAmount());
		order.setState(1);
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		order.setUser(user);
		Collection<CartItemBean> carItemList = cartMode.getCartItems();
		List<OrderItemBean> orderItemList = new ArrayList<>();
		for (CartItemBean cartItem : carItemList) {
			OrderItemBean orderItem = new OrderItemBean();
			orderItem.setItemid(UUIDUtils.getId());
			orderItem.setQuantity(cartItem.getCount());
			orderItem.setTotal(cartItem.getSubTotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			
			orderItemList.add(orderItem);
		}
		order.setOrderItems(orderItemList);
		//保存订单到数据库 使用事务
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn==null) {
			request.setAttribute("msg", "数据库连接错误 请联系管理员!");
			return "jsp/info.jsp";
		}
		DatabaseService service = new DatabaseServiceImpl();
		boolean result = service.saveOrder(conn, order);
		if (result) {
			request.setAttribute("order", order);
			//若保存成功 清空购物车
			cartMode.clearCartMode();
			//显示订单
			return "jsp/order_info.jsp";
		} else {
			request.setAttribute("msg", "保存订单时系统错误，请稍后再试~");
			return "jsp/info.jsp";
		}
	}
	
	private String getOrdersByPaging(HttpServletRequest request, HttpServletResponse response) {
		int pageSize = 3;
		int pageIndex = Integer.valueOf(request.getParameter("pageIndex"));
		if (pageIndex<=0) pageIndex=1;
		
		UserBean user = (UserBean) request.getSession().getAttribute("userBean");
		if (user==null) {//没有登录 请先登录
			request.setAttribute("msg", "请先登录~");
			return "jsp/info.jsp";
		}
		DatabaseService service = new DatabaseServiceImpl();
		OrderPaging orderPaging = service.getOrdersByPaging(user, pageIndex, pageSize);
		request.setAttribute("orderPaging", orderPaging);
		return "jsp/order_list.jsp";
	}
	
	private String findOrderByOid(HttpServletRequest request, HttpServletResponse response) {
		//检查是否已登录
		UserBean user = (UserBean) request.getSession().getAttribute("userBean");
		if (user==null) {//没有登录 请先登录
			request.setAttribute("msg", "请先登录~");
			return "jsp/info.jsp";
		}
		String oid = request.getParameter("oid");
		DatabaseService service = new DatabaseServiceImpl();
		OrderBean order = service.findOrderByOid(user, oid);
		request.setAttribute("order", order);
		return "jsp/order_info.jsp";
	}
	
	private String payOrder(HttpServletRequest request, HttpServletResponse response) {
		//获取数据
		String oid = request.getParameter("oid");
		String address = request.getParameter("address");
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String pd_FrpId = request.getParameter("pd_FrpId");
		//整合数据
		String p0_Cmd = "Buy";
		String p1_MerId = "10001126856";//商户编号
		String p2_Order = oid;//订单编号
		String p3_Amt = "0.01";//金额
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = "http://localhost:8080/StoreOnline/OrderServlet?method=callBack";//接受响应参数的Servlet
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";//公司的秘钥
			
		//调用易宝的加密算法,对所有数据进行加密,返回电子签名
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
				
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		//重定向到支付平台
		try {
			response.sendRedirect(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			request.setAttribute("msg", "订单提交重定向异常~");
			return "jsp/info.jsp";
		}
		return "";
	}
	
	/**
	 * 支付后 支付平台重定向后的回调方法
	 * @param request
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	private String callBack(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("userBean");
		if (user==null) {//没有登录 请先登录
			request.setAttribute("msg", "请先登录~");
			return "jsp/info.jsp";
		}
		//接收易宝支付的数据
		// 验证请求来源和数据有效性
		// 阅读支付结果参数说明
		// System.out.println("==============================================");
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");

		// hmac
		String hmac = request.getParameter("hmac");
		// 利用本地密钥和加密算法 加密数据
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		
		
		//保证数据合法性
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				
				//如果支付成功,更新订单状态
				DatabaseService service=new DatabaseServiceImpl();
				OrderBean order=service.findOrderByOid(user, r6_Order);
				order.setState(2);
				service.updateOrder(order);
				//向request放入提示信息
				request.setAttribute("msg", "支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
				//转发到/jsp/info.jsp
				return "/jsp/info.jsp";
				
				
			} else if (r9_BType.equals("2")) {
				// 修改订单状态:
				// 服务器点对点，来自于易宝的通知
				System.out.println("收到易宝通知，修改订单状态！");//
				// 回复给易宝success，如果不回复，易宝会一直通知
				resp.getWriter().print("success");
			}

		} else {
			throw new RuntimeException("数据被篡改！");
		}
		return "";
	}
}
