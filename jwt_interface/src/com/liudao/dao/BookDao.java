package com.liudao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.liudao.entities.Book;
import com.liudao.utils.JdbcUtils;
/**
 * DAO，实现数据库的实际访问
 * @author liudao
 *
 */
public class BookDao {
	/**
	 * 根据给定的sql和参数进行数据库查询，并将结果转成对象返回
	 * @param sql 需要执行的select语句
	 * @param args select查询语句中占位符所需的参数
	 * @return select查询后返回的对象集合
	 */
	public List<Book> getBooks(String sql, Object...args){
		List<Book> books = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection(); // 获取数据库连接
			ps = conn.prepareStatement(sql); // 创建带有占位符的语句对象
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i+1, args[i]); // 对占位符进行实际赋值
			}
			rs = ps.executeQuery(); // 执行sql语句，并获取返回
			while(rs.next()) { // 根据返回进行记录的逐条遍历
				Book book = new Book();
				book.setBookId(rs.getInt("book_id"));
				book.setBookName(rs.getString("book_name"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookPrice(rs.getDouble("book_price"));
				books.add(book); // 每一条记录代表一个book对象，添加到集合中
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(conn, ps, rs); // 关闭数据库连接 
		}
		return books;
	}
}
