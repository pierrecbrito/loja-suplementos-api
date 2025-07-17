package com.suplementos.lojasuplementosapi.core;


public final class SecurityConstants {
    private SecurityConstants() {}
    
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CLIENTE = "ROLE_CLIENTE";
    
    public static final String HAS_ROLE_ADMIN = "hasAuthority('ROLE_ADMIN')";
    public static final String HAS_ROLE_CLIENTE = "hasAuthority('ROLE_CLIENTE')";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
}