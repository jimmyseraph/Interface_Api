package com.liudao.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;
/**
 * 数据库连接池的常用方法类，数据库连接池的配置信息放在ClassPath路径下的dbcp.properties文件中
 * @author 六道先生
 *
 */
public class JdbcUtils {
	/**
	 * 数据库连接池对象
	 */
	private static DataSource ds;
	static {
		/*
		 * 从ClassPath路径下的dbcp.properties文件中读取数据库连接配置信息，存放到properties对象中
		 */
		Properties prop = new Properties();
		InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("dbcp.properties");
		try {
			prop.load(is);
			ds = BasicDataSourceFactory.createDataSource(prop); // 使用静态工厂创建数据库连接池
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回数据库连接池对象
	 * @return 数据库连接池对象
	 */
	public static DataSource getDataSource() {
		return ds;
	}
	/**
	 * 返回数据库连接对象
	 * @return 数据库连接对象
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = ds.getConnection(); // 从数据库连接池中获取一个空闲的连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接的静态方法
	 * @param conn 需要关闭的连接对象
	 * @param stmt 需要关闭的语句对象 
	 * @param rs 需要关闭的查询结果对象
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn, Statement stmt) {
		close(conn, stmt, null);
	}
	public static void close(Connection conn) {
		close(conn, null, null);
	}
	
}
