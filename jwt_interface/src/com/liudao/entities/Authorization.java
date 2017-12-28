package com.liudao.entities;
/**
 * 返回给成功登录的授权用户当前的token
 * @author liudao
 *
 */
public class Authorization {
	/**
	 * 状态信息
	 */
	private String status;
	/**
	 * token信息
	 */
	private String token;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "Authorization [status=" + status + ", token=" + token + "]";
	}
}
