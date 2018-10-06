package com.store.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.store.bean.OrderBean;
import com.store.bean.OrderItemBean;
import com.store.bean.UserBean;

public interface OrderDao {
	
	/**
	 * 保存订单到数据库
	 * @param conn
	 * @param order
	 * @return
	 * @throws SQLException
	 */
	public int insertOrder(Connection conn, OrderBean order) throws SQLException;
	
	/**
	 * 保存订单中的条目到数据库
	 * @param conn
	 * @param orderItem
	 * @return
	 * @throws SQLException
	 */
	public int insertOrderItem(Connection conn, OrderItemBean orderItem) throws SQLException;
	
	/**
	 * 获取订单总数
	 * @return
	 * @throws SQLException
	 */
	public int getOrderAmount() throws SQLException;
	
	/**
	 * 分页查询订单与订单详情
	 * @param orderIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public List<OrderBean> getOrdersByPaging(UserBean user, int orderIndex, int pageSize) throws SQLException;
	
	/**
	 * 通过oid获取订单详情
	 * @param user
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public OrderBean findOrderByOid(UserBean user, String oid) throws SQLException;
	
	/**
	 * 更新订单
	 * @param order
	 * @return
	 * @throws SQLException
	 */
	public int updateOrder(OrderBean order) throws SQLException;
}
