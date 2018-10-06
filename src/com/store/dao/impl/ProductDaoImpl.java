package com.store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.store.bean.ProductBean;
import com.store.dao.ProductDao;
import com.store.utils.JDBCUtil;

public class ProductDaoImpl implements ProductDao {

	/**
	 * 插入数据(一款产品)到产品表
	 */
	@Override
	public int insertProduct(ProductBean product) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "insert into product value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return runner.update(sql, product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(), product.getPimage(),
				product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(), product.getCid());
	}

	/**
	 * 获取所有商品
	 */
	@Override
	public List<ProductBean> getAllProduct() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product";
		return runner.query(sql, new BeanListHandler<ProductBean>(ProductBean.class));
	}

	/**
	 * 获取一定数量的热门商品
	 */
	@Override
	public List<ProductBean> getHotProducts(int count) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product where pflag=0 and is_hot=1 order by pdate DESC limit 0, ?";
		return runner.query(sql, new BeanListHandler<ProductBean>(ProductBean.class), count);
	}

	/**
	 * 获取一定数量的最新商品
	 */
	@Override
	public List<ProductBean> getNewestProducts(int count) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product where pflag=0 order by pdate DESC limit 0, ?";
		return runner.query(sql, new BeanListHandler<ProductBean>(ProductBean.class), count);
	}

	/**
	 * 通过pid获取商品详情
	 */
	@Override
	public ProductBean getProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product where pid=?";
		return runner.query(sql, new BeanHandler<ProductBean>(ProductBean.class), pid);
	}

	/**
	 * 获取某一类商品的数量
	 */
	@Override
	public int getProductCountByCategory(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long num = (Long) runner.query(sql, new ScalarHandler(), cid);
		return num.intValue();
	}

	/**
	 * 按页码查询某一类商品
	 */
	@Override
	public List<ProductBean> getProductsByCidWithLimit(String cid, int productStart, int pageSize) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product where cid=? limit ?, ?";
		return runner.query(sql, new BeanListHandler<ProductBean>(ProductBean.class), cid, productStart, pageSize);
	}

	/**
	 * 获取所有商品的数量
	 */
	@Override
	public int getProductCount() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select count(*) from product";
		Long num = (Long) runner.query(sql, new ScalarHandler());
		return num.intValue();
	}

	@Override
	public List<ProductBean> getProductsWithLimit(int productStart, int pageSize) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from product limit ?, ?";
		return runner.query(sql, new BeanListHandler<ProductBean>(ProductBean.class), productStart, pageSize);
	}

}
