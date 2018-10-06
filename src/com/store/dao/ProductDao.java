package com.store.dao;

import java.sql.SQLException;
import java.util.List;

import com.store.bean.ProductBean;

public interface ProductDao {

	/**
	 * 插入数据(一款产品)到产品表
	 * @param user
	 * @return
	 */
	public int insertProduct(ProductBean product) throws SQLException;
	
	/**
	 * 获取所有商品
	 * @return
	 * @throws SQLException
	 */
	public List<ProductBean> getAllProduct() throws SQLException;
	
	/**
	 * 获取一定数量的热门商品
	 * @param count 需求数量
	 * @return
	 * @throws SQLException
	 */
	public List<ProductBean> getHotProducts(int count) throws SQLException;
	
	/**
	 * 获取一定数量的最新商品
	 * @param count 需求数量
	 * @return
	 * @throws SQLException
	 */
	public List<ProductBean> getNewestProducts(int count) throws SQLException;
	
	/**
	 * 通过pid获取商品详情
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public ProductBean getProductByPid(String pid) throws SQLException;
	
	/**
	 * 获取某一类商品的数量
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int getProductCountByCategory(String cid) throws SQLException;
	
	/**
	 * 按页码查询某一类商品
	 * @param cid
	 * @param productStart
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public List<ProductBean> getProductsByCidWithLimit(String cid, int productStart, int pageSize) throws SQLException;
	
	/**
	 * 获取所有商品的数量
	 * @return
	 * @throws SQLException
	 */
	public int getProductCount() throws SQLException;
	
	/**
	 * 按页码查询商品
	 * @param productStart
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public List<ProductBean> getProductsWithLimit(int productStart, int pageSize) throws SQLException;
}
