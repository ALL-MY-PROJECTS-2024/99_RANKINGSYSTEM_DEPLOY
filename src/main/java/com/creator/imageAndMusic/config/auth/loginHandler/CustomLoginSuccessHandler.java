package com.creator.imageAndMusic.config.auth.loginHandler;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.config.auth.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler  {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess()");

        //--------------------------------------
        //JWT ADD
        //--------------------------------------
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 쿠키 생성
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, tokenInfo.getAccessToken());
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
        cookie.setPath("/");
        response.addCookie(cookie);
        //--------------------------------------


        Collection<? extends GrantedAuthority> collection =  authentication.getAuthorities();
        collection.forEach( (role)->{
            System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess() role : " + role);
            String role_str =  role.getAuthority();

            try {
                response.sendRedirect("/");
            }catch(Exception e){
                e.printStackTrace();
            }

        });


    }

}

