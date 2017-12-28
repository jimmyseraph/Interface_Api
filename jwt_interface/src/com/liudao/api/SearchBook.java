package com.liudao.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liudao.entities.Book;
import com.liudao.entities.SearchBookResult;
import com.liudao.entities.SearchError;
import com.liudao.services.SearchBookService;
import com.liudao.utils.BearerToken;

/**
 * Servlet implementation class SearchBook
 * 查询书籍的api接口，要求必须是授权用户才可以查询，必需提供cookie，内容为token的过期时间，body部分必需传递user参数表示用户名，
 * header部分必需有Authorization，内容为bearer token字段。
 * 查询通过书籍id、书籍name、书籍作者author、书籍价格price参数进行
 */
@WebServlet(name = "sbook", urlPatterns = { "/sbook" })
public class SearchBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new GsonBuilder().create();
		String json = null;
		request.setCharacterEncoding("utf-8");
		String authToken = (String) request.getAttribute("token"); // 获取request中保存的属性token
		Object obj = getServletContext().getAttribute("secretKey"); // 从服务端的servlet上下文中获取secretKey属性
		Date expir = new Date((long)request.getAttribute("expir")); // 获request中保存的属性——过期时间毫秒数，并转为date对象
		String user = request.getParameter("user"); // 从request参数中取出用户名
		SecretKey key = (SecretKey)obj; // 获取私钥
		String token = BearerToken.createToken(key, user, expir); // 通过用户名和过期时间重新计算token
		if(token.equals(authToken)) { // 将重新计算的token和请求中传递的token进行对比，如果一致则表示该请求合法
			// 获取请求中传递的id、name、author、price
			String bookId = request.getParameter("id");
			String bookName = request.getParameter("name");
			String bookAuthor = request.getParameter("author");
			String bookPrice = request.getParameter("price");
			// 调用SearchBookService获取查询结果
			SearchBookService sbs = new SearchBookService();
			List<Book> books = sbs.getBooks(bookId, bookName, bookAuthor, bookPrice);
			if(books.size() > 0) { // 如果返回的结果不是空集合，则表示查询成功
				//设置返回结构
				SearchBookResult sbr = new SearchBookResult();
				sbr.setCount(books.size());
				sbr.setBooks(books);
				json = gson.toJson(sbr);
			}else {
				// 如果是空集合，则表示未找到，设置错误返回
				SearchError se = new SearchError();
				se.setErrorCode(200);
				se.setMessage("未找到指定的内容");
				json = gson.toJson(se);
			}			
		}else { // 如果两个token的值不一致，则表示认证失败
			SearchError se = new SearchError();
			se.setErrorCode(105);
			se.setMessage("认证失败，请确定token是否有效");
			json = gson.toJson(se);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

}
