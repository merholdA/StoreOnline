package com.store.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.store.bean.UserBean;
import com.store.dao.UserDao;
import com.store.utils.JDBCUtil;

public class UserDaoImpl implements UserDao {

	/**
	 * 插入数据(一个新的用户)到用户表
	 */
	@Override
	public int insertUser(UserBean user) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "insert into user value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return runner.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
				user.getBirthday(), user.getSex(), user.getState(), user.getCode(), user.getTelephone());
	}

	/**
	 * 通过激活码查找用户
	 */
	@Override
	public UserBean getUserByCode(String code) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from user where code=?";
		return runner.query(sql, new BeanHandler<UserBean>(UserBean.class), code);
	}

	/**
	 * 修改用户的激活状态state
	 */
	@Override
	public int updateUserState(String uid, int state) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "update user set state=? where uid=?";
		return runner.update(sql, state, uid);
	}

	/**
	 * 通过用户名和密码查找用户
	 */
	@Override
	public UserBean getUserByUsernamePassword(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil.getDataSource());
		String sql = "select * from user where username=? and password=?";
		return runner.query(sql, new BeanHandler<UserBean>(UserBean.class), username, password);
	}

}
