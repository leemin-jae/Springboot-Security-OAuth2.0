package com.cos.security_basic.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEamil();
    String getName();

}
