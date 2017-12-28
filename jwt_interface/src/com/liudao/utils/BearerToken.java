package com.liudao.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * 用于生成token
 * @author liudao
 *
 */
public class BearerToken {
	/**
	 * 生成token的静态方法 
	 * @param key 加密私钥
	 * @param username 授权用户的用户名
	 * @param expir token过期时间
	 * @return token字符串
	 */
	public static String createToken(SecretKey key, String username, Date expir) {
		SignatureAlgorithm sa = SignatureAlgorithm.HS256; // 设置签名算法
		JwtBuilder builder = Jwts.builder()
				.setAudience(username) // 授权用户
				.setExpiration(expir) // 过期时间
				.signWith(sa, key); // 签名算法和私钥 
		return builder.compact();
	}
}
