package com.store.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtil {

	private static ComboPooledDataSource mCPDS;
	{
		mCPDS = new ComboPooledDataSource();//数据库连接池
	}
	
	/**
	 * 返回数据库连接池对象DataSource
	 * @return
	 */
	public static synchronized DataSource getDataSource() {
		if (mCPDS==null) mCPDS = new ComboPooledDataSource();
		return mCPDS;
	}
	
	/**
	 * 返回连接
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		if (mCPDS!=null) conn = mCPDS.getConnection();
		return conn;
	}
}
