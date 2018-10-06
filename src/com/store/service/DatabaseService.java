package com.store.service;

import java.sql.Connection;
import java.util.List;

import com.store.bean.CategoryBean;
import com.store.bean.OrderBean;
import com.store.bean.OrderPaging;
import com.store.bean.ProductBean;
import com.store.bean.ProductPagingBean;
import com.store.bean.ProductPagingCgyBean;
import com.store.bean.UserBean;

/**
 * 数据库部分的Service
 * @author Administrator
 *
 */
public interface DatabaseService {

	/**
	 * 添加用户到数据库
	 * @param user
	 * @return true:添加成功 false:添加失败
	 */
	public boolean addUser(UserBean user);
	
	/**
	 * 激活用户
	 * @param code 0:成功 1.用户不存在 2.已激活  3.激活失败-其它原因
	 * @return
	 */
	public int activateUser(String code);
	
	/**
	 * 通过用户名和密码查找用户
	 * @param username
	 * @param password
	 * @return
	 */
	public UserBean getUserByUsernamePassword(String username, String password);
	
	/**
	 * 获取所有的分类
	 * @return
	 */
	public List<CategoryBean> getAllCategory();
	
	/**
	 * 获取一定数量的热门商品
	 * @param count
	 * @return
	 */
	public List<ProductBean> getHotProducts(int count);
	
	/**
	 * 获取一定数量的最新商品
	 * @param count
	 * @return
	 */
	public List<ProductBean> getNewestProducts(int count);
	
	/**
	 * 通过pid获取商品详情
	 * @param pid
	 * @return
	 */
	public ProductBean getProductByPid(String pid);
	
	/**
	 * 通过cid获取分类信息
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public CategoryBean getCategoryById(String cid);
	
	/**
	 * 根据商品类型获取产品分页信息
	 * @param cid
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public ProductPagingCgyBean getProtByCgyWithPaging(String cid, int pageSize, int pageIndex);
	
	/**
	 * 保存某一订单信息
	 * @param order
	 * @return
	 */
	public boolean saveOrder(Connection conn, OrderBean order);
	
	/**
	 * 分页查询订单信息
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public OrderPaging getOrdersByPaging(UserBean user, int pageIndex, int pageSize);
	
	/**
	 * 根据oid获取订单详情
	 * @param user
	 * @param oid
	 * @return
	 */
	public OrderBean findOrderByOid(UserBean user, String oid);
	
	/**
	 * 修改订单
	 * @param order
	 * @return
	 */
	public int updateOrder(OrderBean order);

	/**
	 * 添加分类信息
	 * @param category
	 * @return
	 */
	public int addCategory(CategoryBean category);

	/**
	 * 分页查询商品信息
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public ProductPagingBean getProductsByPaging(int pageSize, int pageIndex);

	/**
	 * 添加产品
	 * @param product
	 */
	public int addProduct(ProductBean product);
}
