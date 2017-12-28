package com.liudao.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liudao.entities.SearchError;

/**
 * Servlet Filter implementation class AuthFilter
 */
@WebFilter(filterName = "authFilter", urlPatterns = { "/sbook" })
public class AuthFilter implements Filter {
	private ServletContext servletContext;
    /**
     * Default constructor. 
     */
    public AuthFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		System.out.println("--->验证信息过滤");
		Gson gson = new GsonBuilder().create();
		String json = null;
		SearchError error = null;
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpRequest.setCharacterEncoding("utf-8");
		String authToken = httpRequest.getHeader("Authorization"); // 获取用户请求中传递的header：Authorization
		String username = httpRequest.getParameter("user");
		Object keyObj = servletContext.getAttribute("secretKey"); // 从服务端的servlet上下文中获取secretKey属性
		Cookie[] cookies = httpRequest.getCookies(); // 获取客户端请求中所附加的所有cookie
		Cookie cookie = null;
		if(cookies != null) { // 如果客户端有cookie传递到服务端，则对所有的cookie进行检测，是否有名为expir的cookie
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals("expir")) { // 如果找到expir的cookie，则退出循环
					cookie = cookies[i];
					break;
				}
			}
		}
		if(authToken == null || !authToken.startsWith("Bearer ")) { // 检查请求中是否有token，并且符合Bearer Token格式
			error = new SearchError();
			error.setErrorCode(100);
			error.setMessage("缺少认证信息——Token");
		}else if(keyObj == null) { // 检查服务端是否保存有密钥
			error = new SearchError();
			error.setErrorCode(101);
			error.setMessage("服务器已重置，请重新登录获取Token");
		}else if(cookie == null) { // 检查请求中是否传递名字为expir的Cookie
			error = new SearchError();
			error.setErrorCode(102);
			error.setMessage("Token已过期");
		}else if(username == null) { // 检查请求中是否传递参数user
			error = new SearchError();
			error.setErrorCode(103);
			error.setMessage("缺少认证信息——用户名");
		}else {
			String expirStr = cookie.getValue(); // 获取cookie中expir的值
			long expir = 0L;
			try {
				expir = Long.parseLong(expirStr); // 将expir从字符串转为long类型
			}catch(NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			if(expir < System.currentTimeMillis()) { // 判断是否过期
				error = new SearchError();
				error.setErrorCode(102);
				error.setMessage("Token已过期");
			}else {
				// 将过期时间和token作为请求（request）的属性保存起来，以便之后的servlet可以直接获取使用
				httpRequest.setAttribute("expir", expir);
				// 由于Authorization的值是Bearer XXXXX，要将XXXX的值取出，需要去掉前面7个字符
				httpRequest.setAttribute("token", authToken.substring(7)); 
			}
		}
		if(error != null) { // 如果error对象不为空，则直接进行返回，将错误信息的json字符串发给客户端
			httpResponse.setCharacterEncoding("utf-8");
			httpResponse.setContentType("application/json");
			PrintWriter out = httpResponse.getWriter();
			json = gson.toJson(error);
			out.print(json);
			out.flush();
		}else { // 如果error没有内容，则说明检查通过，允许进入下一个过滤器
			chain.doFilter(httpRequest, httpResponse);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		servletContext = fConfig.getServletContext();
	}

}
