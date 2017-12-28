package com.liudao.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liudao.entities.Authorization;
import com.liudao.entities.SearchError;
import com.liudao.utils.BearerToken;
import com.liudao.utils.CreateKey;

/**
 * Servlet implementation class Login
 * 实现用户登录授权的接口，要求用户通过post请求传递user和pwd两个参数，分别代表用户的注册用户名和密码。
 * 如果用户名和密码校验成功，则会生成一小时有效的token，并将token返回给用户，同时在用户端生成cookie，用于记录过期时间。
 */
@WebServlet(name="login", urlPatterns= {"/login"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("user");
		String password = request.getParameter("pwd");
		Gson gson = new GsonBuilder().create();
		String json = null; 
		if(username!=null && password!=null) { // 如果用户名和密码不为空
			if(username.equals("liudao") && password.equals("123456")) { // 目前没有使用数据库数据来校验用户名和密码，只是简单的写死了用户名和密码
				SecretKey key = null;
				Object obj =  getServletContext().getAttribute("secretKey"); // 从servlet上下文中获取secretKey
				if(obj == null) { // 如果没有secretKey属性，则说明之前从未生成过服务端加密用的私钥
					key = CreateKey.genKey(); // 获取一个新的私钥
					getServletContext().setAttribute("secretKey", key); // 将该私钥存入服务器的servlet上下文属性中
				}else { // 如果secretKey属性已经设置过，则直接获取
					key = (SecretKey)obj;
				}
				Date expir = new Date(System.currentTimeMillis()+3600000); // 设置token过期时间
				Authorization auth = new Authorization();
				auth.setStatus("authoried"); // 将返回对象的status属性设为已授权
				auth.setToken(BearerToken.createToken(key, username, expir)); // 调用createToken方法对用户名和过期时间进行签名生成token
				json = gson.toJson(auth); // 将auth对象转成json格式
				Cookie cookie = new Cookie("expir", expir.getTime()+""); // 设置客户端需要保存的cookie，名字是expir，值是过期时间的毫秒表达式
				cookie.setMaxAge(3600); // 设置cookie的过期时间是1小时
				response.addCookie(cookie); // 将cookie发送到客户端
			}else { // 如果用户名和密码不正确
				// 设置错误返回对象
				SearchError error = new SearchError(); 
				error.setErrorCode(106);
				error.setMessage("错误的用户名或密码");
				json = gson.toJson(error);
			}
		}else { // 当未提交用户名或者密码，则设置错误返回对象
			SearchError error = new SearchError();
			error.setErrorCode(106);
			error.setMessage("未提供用户名和密码进行身份验证");
			json = gson.toJson(error);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

}
