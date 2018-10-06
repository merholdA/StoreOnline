package com.store.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.store.bean.OrderBean;
import com.store.bean.OrderItemBean;
import com.store.bean.ProductBean;
import com.store.bean.UserBean;
import com.store.dao.OrderDao;
import com.store.utils.JDBCUtil;

public class OrderDaoImpl implements OrderDao {

	/**
	 * 保存订单到数据库
	 */
	@Override
	public int insertOrder(Connection conn, OrderBean order) throws SQLException {
		String sql="INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
		QueryRunner qr=new QueryRunner();
		Object[] params={order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(), order.getAddress(),
				order.getName(), order.getTelephone(), order.getUser().getUid()};
		return qr.update(conn,sql,params);
	}

	/**
	 * 保存订单中的条目到数据库
	 */
	@Override
	public int insertOrderItem(Connection conn, OrderItemBean orderItem) throws SQLException {
		String sql="INSERT INTO orderitem VALUES(?,?,?,?,?)";
		QueryRunner qr=new QueryRunner();
		Object[] params={orderItem.getItemid(), orderItem.getQuantity(), orderItem.getTotal(),
				orderItem.getProduct().getPid(), orderItem.getOrder().getOid()};
		return qr.update(conn,sql,params);
	}

	/**
	 * 获取订单总数
	 */
	@Override
	public int getOrderAmount() throws SQLException {
		String sql = "select count(*) from orders";
		QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
		Long num = (Long) qr.query(sql, new ScalarHandler());
		return num.intValue();
	}

	/**
	 * 分页查询订单与订单详情
	 */
	@Override
	public List<OrderBean> getOrdersByPaging(UserBean user,int orderIndex, int pageSize) throws SQLException {
		List<OrderBean> orderList = null;
		String sql = "select * from orders where uid=? limit ?, ?";
		QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
		orderList = qr.query(sql, new BeanListHandler<OrderBean>(OrderBean.class), user.getUid(), orderIndex, pageSize);
		if (orderList!=null) {//订单详情
			for (OrderBean order : orderList) {
				order.setOrderItems(new ArrayList<OrderItemBean>());
				order.setUser(user);
				String oid = order.getOid();
				if (null!=oid && oid.length()>0) {
					String sqlP = "select * from orderitem o, product p where o.pid=p.pid and o.oid=?";
					List<Map<String, Object>> mapList = qr.query(sqlP, new MapListHandler(), oid);
					for (Map<String, Object> map : mapList) {
						ProductBean product = new ProductBean();
						OrderItemBean orderItem = new OrderItemBean();
						// 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
						// 1_创建时间类型的转换器
						DateConverter dt = new DateConverter();
						// 2_设置转换的格式
						dt.setPattern("yyyy-MM-dd");
						// 3_注册转换器
						ConvertUtils.register(dt, java.util.Date.class);
						
						//将map中属于orderItem的数据自动填充到orderItem对象上
						try {
							BeanUtils.populate(orderItem, map);
							BeanUtils.populate(product, map);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						
						orderItem.setProduct(product);
						order.getOrderItems().add(orderItem);
					}
				}
			}
		}
		return orderList;
	}

	@Override
	public OrderBean findOrderByOid(UserBean user, String oid) throws SQLException {
		String sql = "select * from orders where oid=? and uid=?";
		QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
		OrderBean order = qr.query(sql, new BeanHandler<OrderBean>(OrderBean.class), oid, user.getUid());
		if (order!=null) {
			order.setOrderItems(new ArrayList<OrderItemBean>());
			order.setUser(user);
			String sqlP = "select * from orderitem o, product p where o.pid=p.pid and o.oid=?";
			List<Map<String, Object>> mapList = qr.query(sqlP, new MapListHandler(), oid);
			for (Map<String, Object> map : mapList) {
				ProductBean product = new ProductBean();
				OrderItemBean orderItem = new OrderItemBean();
				// 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
				// 1_创建时间类型的转换器
				DateConverter dt = new DateConverter();
				// 2_设置转换的格式
				dt.setPattern("yyyy-MM-dd");
				// 3_注册转换器
				ConvertUtils.register(dt, java.util.Date.class);
				
				//将map中属于orderItem的数据自动填充到orderItem对象上
				try {
					BeanUtils.populate(orderItem, map);
					BeanUtils.populate(product, map);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				orderItem.setProduct(product);
				order.getOrderItems().add(orderItem);
			}
		}
		return order;
	}

	/**
	 * 更新订单
	 */
	@Override
	public int updateOrder(OrderBean order) throws SQLException {
		String sql="update orders set ordertime=? ,total=? ,state= ?, address=?, name=?, telephone =? where oid=?";
		QueryRunner qr=new QueryRunner(JDBCUtil.getDataSource());
		Object[] params={order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		return qr.update(sql,params);
	}

}
