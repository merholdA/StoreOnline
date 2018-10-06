package com.store.dao;

import java.sql.SQLException;

import com.store.bean.UserBean;

public interface UserDao {

	/**
	 * 插入数据(一个新的用户)到用户表
	 * @param user
	 * @return
	 */
	public int insertUser(UserBean user) throws SQLException;
	
	/**
	 * 通过激活码code查找用户
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public UserBean getUserByCode(String code) throws SQLException;
	
	/**
	 * 通过用户名和密码查找用户
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public UserBean getUserByUsernamePassword(String username, String password) throws SQLException;
	
	/**
	 * 修改用户的激活状态state
	 * @param uid
	 * @param state
	 * @return
	 * @throws SQLException
	 */
	public int updateUserState(String uid, int state) throws SQLException;
}
