package com.liudao.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liudao.entities.SearchError;

/**
 * Servlet Filter implementation class ParametersFilter
 */
@WebFilter(filterName = "parametersFilter", urlPatterns = { "/sbook" })
public class ParametersFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ParametersFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		System.out.println("--->参数格式过滤");
		Gson gson = new GsonBuilder().create();
		String json = null;
		SearchError error = null;
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String id = httpRequest.getParameter("id"); // 从请求中取出参数id
		String price = httpRequest.getParameter("price"); // 从请求中取出参数price
		if(id != null) { //如果id不为空，则尝试是否为int型
			try {
				Integer.parseInt(id);
			}catch (NumberFormatException nfe) {
				error = new SearchError();
				error.setErrorCode(104);
				error.setMessage("id参数必需为整数");
			}
		}
		if(price != null) { //如果price不为空，则尝试是否为double型
			try {
				Double.parseDouble(price);
			}catch (NumberFormatException nfe) {
				error = new SearchError();
				error.setErrorCode(104);
				error.setMessage("price参数必需为数字");
			}
		}
		if(error != null) { // 如果error对象不为空，则直接进行返回，将错误信息的json字符串发给客户端
			httpResponse.setCharacterEncoding("utf-8");
			httpResponse.setContentType("application/json");
			PrintWriter out = httpResponse.getWriter();
			json = gson.toJson(error);
			out.print(json);
			out.flush();
		}else { // 如果error没有内容，则说明检查通过，允许进入下一步
			chain.doFilter(httpRequest, httpResponse);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
