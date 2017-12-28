package com.liudao.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * 用于生成私钥
 * @author liudao
 *
 */
public class CreateKey {
	/**
	 * 生成私钥的静态方法
	 * @return
	 */
	public static SecretKey genKey() {
		String seed = System.currentTimeMillis()+""; // 私钥的生成是根据当前系统时间的毫秒表达式
		return new SecretKeySpec(seed.getBytes(), "AES"); //通过AES算法将字节数组加密为私钥 
	}
}
