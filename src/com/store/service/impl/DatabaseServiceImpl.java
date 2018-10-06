package com.store.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.store.bean.CategoryBean;
import com.store.bean.OrderBean;
import com.store.bean.OrderItemBean;
import com.store.bean.OrderPaging;
import com.store.bean.ProductBean;
import com.store.bean.ProductPagingBean;
import com.store.bean.ProductPagingCgyBean;
import com.store.bean.UserBean;
import com.store.dao.CategoryDao;
import com.store.dao.OrderDao;
import com.store.dao.ProductDao;
import com.store.dao.UserDao;
import com.store.dao.impl.CategoryDaoImpl;
import com.store.dao.impl.OrderDaoImpl;
import com.store.dao.impl.ProductDaoImpl;
import com.store.dao.impl.UserDaoImpl;
import com.store.service.DatabaseService;

public class DatabaseServiceImpl implements DatabaseService {

	/**
	 * 添加用户到数据库
	 */
	@Override
	public boolean addUser(UserBean user) {
		boolean result = false;
		UserDao dao = new UserDaoImpl();
		try {
			if (dao.insertUser(user)>0) result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 激活用户
	 */
	@Override
	public int activateUser(String code) {
		int result = 0;
		UserDao dao = new UserDaoImpl();
		UserBean user = null;
		try {
			user = dao.getUserByCode(code);
		} catch (SQLException e) {
			result = 1;
			e.printStackTrace();
		}
		if (user!=null) {
			if (user.getState()==0) {//未激活 --- 修改state 激活用户
				try {
					if (dao.updateUserState(user.getUid(), 1)<=0) result = 3;
				} catch (SQLException e) {
					result = 3;
					e.printStackTrace();
				}
			} else {//已激活
				result = 2;
			}
		} else {
			result = 1;
		}
		return result;
	}

	/**
	 * 通过用户名和密码查找用户
	 */
	@Override
	public UserBean getUserByUsernamePassword(String username, String password) {
		UserBean user = null;
		UserDao dao = new UserDaoImpl();
		try {
			user = dao.getUserByUsernamePassword(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 获取所有的分类
	 */
	@Override
	public List<CategoryBean> getAllCategory() {
		List<CategoryBean> categoryList = null;
		CategoryDao dao = new CategoryDaoImpl();
		try {
			categoryList = dao.getAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoryList;
	}

	/**
	 * 获取一定数量的热门商品
	 */
	@Override
	public List<ProductBean> getHotProducts(int count) {
		List<ProductBean> productList = null;
		ProductDao dao = new ProductDaoImpl();
		try {
			productList = dao.getHotProducts(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	/**
	 * 获取一定数量的最新商品
	 */
	@Override
	public List<ProductBean> getNewestProducts(int count) {
		List<ProductBean> productList = null;
		ProductDao dao = new ProductDaoImpl();
		try {
			productList = dao.getNewestProducts(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	/**
	 * 通过pid获取商品详情
	 */
	@Override
	public ProductBean getProductByPid(String pid) {
		ProductBean product = null;
		ProductDao dao = new ProductDaoImpl();
		try {
			product = dao.getProductByPid(pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * 通过cid获取分类信息
	 */
	@Override
	public CategoryBean getCategoryById(String cid) {
		CategoryBean category = null;
		CategoryDao dao = new CategoryDaoImpl();
		try {
			category = dao.getCategoryById(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	@Override
	public ProductPagingCgyBean getProtByCgyWithPaging(String cid, int pageSize, int pageIndex) {
		ProductPagingCgyBean ppcBean = null;
		ProductDao dao = new ProductDaoImpl();
		int productCount = 0;
		List<ProductBean> productList = null;
		try {
			productCount = dao.getProductCountByCategory(cid);
			productList = dao.getProductsByCidWithLimit(cid, (pageIndex-1)*pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ppcBean = new ProductPagingCgyBean(cid, getCategoryById(cid).getCname(), pageSize, productCount/pageSize, pageIndex);
		ppcBean.setProductList(productList);
		return ppcBean;
	}

	/**
	 * 保存订单与订单项
	 */
	@Override
	public boolean saveOrder(Connection conn, OrderBean order) {
		boolean result = false;
		int orderCount = 0;
		int orderItemCount = 0;
		try {
			conn.setAutoCommit(false);
			OrderDao dao = new OrderDaoImpl();
			orderCount = dao.insertOrder(conn, order);
			for (OrderItemBean orderItem : order.getOrderItems()) {
				orderItemCount += dao.insertOrderItem(conn, orderItem);
			}
		} catch (SQLException e) {
			try {conn.rollback(); }
			catch (SQLException e1) { e1.printStackTrace(); }
			e.printStackTrace();
		}
		if (orderCount>0 && orderItemCount==order.getOrderItems().size()) {
			result = true;
			try { conn.commit(); }
			catch (SQLException e) { e.printStackTrace(); }
		} else {
			try {conn.rollback(); }
			catch (SQLException e1) { e1.printStackTrace(); }
		}
		return result;
	}

	/**
	 * 分页查询订单信息
	 */
	@Override
	public OrderPaging getOrdersByPaging(UserBean user, int pageIndex, int pageSize) {
		int pageCount = 0;
		List<OrderBean> orderList = new ArrayList<>();
		OrderPaging orderPaging = new OrderPaging();
		
		OrderDao dao = new OrderDaoImpl();
		try {
			int orderSum = dao.getOrderAmount();
			pageCount = orderSum/pageSize;
			if (orderSum%pageSize>0) pageCount+=1;
			orderList = dao.getOrdersByPaging(user, (pageIndex-1)*pageSize, pageSize);
		} catch (SQLException e) { e.printStackTrace(); }
		
		orderPaging.setPageIndex(pageIndex);
		orderPaging.setPageSize(pageSize);
		orderPaging.setPageCount(pageCount);
		orderPaging.setOrderList(orderList);
		return orderPaging;
	}

	/**
	 * 根据oid获取订单详情
	 */
	@Override
	public OrderBean findOrderByOid(UserBean user, String oid) {
		OrderBean order = null;
		OrderDao dao = new OrderDaoImpl();
		try {
			order = dao.findOrderByOid(user, oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * 修改订单
	 */
	@Override
	public int updateOrder(OrderBean order) {
		int result = -1;
		
		OrderDao dao = new OrderDaoImpl();
		try {
			result = dao.updateOrder(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 添加分类信息
	 */
	@Override
	public int addCategory(CategoryBean category) {
		int result = -1;
		CategoryDao dao = new CategoryDaoImpl();
		try {
			result = dao.insertCategory(category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 分页查询商品信息
	 */
	@Override
	public ProductPagingBean getProductsByPaging(int pageSize, int pageIndex) {
		ProductPagingBean ppBean = null;
		ProductDao dao = new ProductDaoImpl();
		int productCount = 0;
		int pageCount = 0;
		List<ProductBean> productList = null;
		try {
			productCount = dao.getProductCount();
			productList = dao.getProductsWithLimit((pageIndex-1)*pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageCount = productCount/pageSize;
		if (productCount%pageSize!=0) {
			pageCount += 1;
		}
		ppBean = new ProductPagingBean();
		ppBean.setPageIndex(pageIndex);
		ppBean.setPageSize(pageSize);
		ppBean.setPageCount(pageCount);
		ppBean.setProductList(productList);
		return ppBean;
	}

	/**
	 * 添加产品
	 */
	@Override
	public int addProduct(ProductBean product) {
		int result = -1;
		ProductDao dao = new ProductDaoImpl();
		try {
			result = dao.insertProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	
}
