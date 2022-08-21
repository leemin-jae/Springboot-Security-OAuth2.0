package com.cos.security_basic.oauth;

import com.cos.security_basic.auth.PrincipalDetails;
import com.cos.security_basic.config.encoderService;
import com.cos.security_basic.model.User;
import com.cos.security_basic.oauth.provider.FacebookUserInfo;
import com.cos.security_basic.oauth.provider.GoogleUserInfo;
import com.cos.security_basic.oauth.provider.NaverUserInfo;
import com.cos.security_basic.oauth.provider.OAuth2UserInfo;
import com.cos.security_basic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    encoderService encoderService;
    //구글로 받은 userRequest 데이터에 대해 후처리하는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken   : " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());

        ///////////////

        OAuth2UserInfo oAuth2Userinfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2Userinfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2Userinfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2Userinfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }else{
                System.out.println("지원하지 않는 요청 요청");
            }

        String provider = oAuth2Userinfo.getProvider();
        String providerId = oAuth2Userinfo.getProviderId();
        String username = provider + "_" + providerId; // google_123213213
        String password = encoderService.passwordEncoder().encode("test");
        String email = oAuth2Userinfo.getEamil();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("이미 로그인된 유저입니다.");
        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
