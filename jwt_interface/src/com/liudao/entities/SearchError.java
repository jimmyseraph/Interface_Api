package com.liudao.entities;
/**
 * 用于返回用户各类访问错误的实例
 * @author 六道
 *
 */
public class SearchError {
	/**
	 * 错误编号
	 */
	private int errorCode;
	/**
	 * 错误信息
	 */
	private String message;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "SearchError [errorCode=" + errorCode + ", message=" + message + "]";
	}
}
