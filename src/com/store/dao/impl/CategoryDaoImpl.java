package com.store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.store.bean.CategoryBean;
import com.store.dao.CategoryDao;
import com.store.utils.JDBCUtil;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public int insertCategory(CategoryBean category) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "insert into category value(?, ?)";
		return runner.update(sql, category.getCid(), category.getCname());
	}

	@Override
	public List<CategoryBean> getAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from category";
		return runner.query(sql, new BeanListHandler<CategoryBean>(CategoryBean.class));
	}

	@Override
	public CategoryBean getCategoryById(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from category where cid=?";
		return runner.query(sql, new BeanHandler<CategoryBean>(CategoryBean.class), cid);
	}

}
