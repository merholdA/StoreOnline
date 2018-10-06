package com.store.dao;

import java.sql.SQLException;
import java.util.List;

import com.store.bean.CategoryBean;

public interface CategoryDao {

	/**
	 * 插入数据(一个新的分类)到产品分类表
	 * @param user
	 * @return
	 */
	public int insertCategory(CategoryBean category) throws SQLException;
	
	/**
	 * 获取所有的分类
	 * @return
	 * @throws SQLException
	 */
	public List<CategoryBean> getAllCategory() throws SQLException;
	
	/**
	 * 通过cid获取分类信息
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public CategoryBean getCategoryById(String cid) throws SQLException;
}
