package com.liudao.services;

import java.util.ArrayList;
import java.util.List;

import com.liudao.dao.BookDao;
import com.liudao.entities.Book;
/**
 * 查询数据库中书籍信息的服务
 * @author liudao
 *
 */
public class SearchBookService {
	/**
	 * 用于对输入指定筛选条件信息数据库查询的方法
	 * @param bookId 对应书籍的id，可以为空
	 * @param bookName 对应书籍的名字，支持模糊查询，可为空
	 * @param bookAuthor 对应书籍的作者，支持模糊查询，可为空
	 * @param bookPrice 对应书籍的价格，表示不超过这个价格的书籍，可为空
	 * @return 所有符合条件的book对象集合
	 */
	public List<Book> getBooks(String bookId, String bookName, String bookAuthor, String bookPrice){
		List<Book> books = null;
		String sql = "select * from program_book where "; // 不完整的sql语句，最后的where用于和之后的筛选条件拼接
		List<Object> listArgs = new ArrayList<>(); // 用于输入到sql中的占位参数，对应sql中“？”占位符
		if(bookId!=null && !bookId.equals("")) { // 如果bookid不为空
			int id = Integer.parseInt(bookId); // 将id转为int类型
			sql = sql + "book_id=? and ";  // sql语句中拼接bookid的筛选条件
			listArgs.add(id); // 将id的值加入到参数中
		}
		if(bookName!=null && !bookName.equals("")) { // 如果bookname不为空
			sql = sql + "book_name like ? and "; // 拼接book_name的筛选条件
			listArgs.add("%"+bookName+"%"); // 将模糊查询条件加入参数集合中
		}
		if(bookAuthor!=null && !bookAuthor.equals("")) { // 如果bookauthor不为空
			sql = sql + "book_author like ? and "; // 拼接book_author的筛选条件
			listArgs.add("%"+bookAuthor+"%"); // 将模糊查询条件加入参数集合中
		}
		if(bookPrice!=null && !bookPrice.equals("")) { // 如果bookprice不为空
			double price = Double.parseDouble(bookPrice); 
			sql = sql + "book_price<=? and "; // 拼接book_price的筛选条件
			listArgs.add(price); //将price的值加入到参数中
		}
		sql = sql + "1=1"; // 最后拼接1=1恒true表达式，用于结尾，否则sql最后留下一个and关键字，语法错误
		BookDao bookDao = new BookDao();
		books = bookDao.getBooks(sql, listArgs.toArray()); // 将组织好的sql，和传递的参数交给DAO进行数据库实际访问
		return books;
	}
}
