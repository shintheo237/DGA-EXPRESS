package com.shintheo.dgae.common;

public class JWTutil {
	public static final String SECRET="DGA-Express@2022";
	public static final String AUTH_HEADER="Authorization";
	public static final long EXPIRE_ACCESS_TOKEN= 30*60*1000;
	public static final long EXPIRE_REFRESH_TOKEN = 15*60*1000;
	public static final String PREFIX = "Bearer ";
}
