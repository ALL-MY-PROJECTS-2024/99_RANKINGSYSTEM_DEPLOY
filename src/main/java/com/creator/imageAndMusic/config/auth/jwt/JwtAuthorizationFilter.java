package com.creator.imageAndMusic.config.auth.jwt;


import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;

/**
 * JWT를 이용한 인증
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(
            UserRepository memberRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException, IOException {


        //System.out.println("[JWTAUTHORIZATIONFILTER] doFilterInternal...");

        String token = null;
        String importAuth = null;

        try{

            if(token==null) {
                // cookie 에서 JWT token을 가져옵니다.
                token = Arrays.stream(request.getCookies())
                        .filter(c -> c.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                        .map(c -> c.getValue())
                        .orElse(null);
            }

        } catch (Exception ignored) {
            //일반적으로 접근하는 요청 URI에 대한 쿠키 예외는 무시한다..

        }

        if (token != null) {
            try {
                if(jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //System.out.println("[JWTAUTHORIZATIONFILTER] : " + authentication);
                }
            } catch (ExpiredJwtException e)     //토큰만료시 예외처리(쿠키 제거)
            {

                //System.out.println("[JWTAUTHORIZATIONFILTER] : ...ExpiredJwtException ...."+e.getMessage());

                //토큰 만료시 처리(Refresh-token으로 갱신처리는 안함-쿠키에서 제거)
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);



            }catch(Exception e2){

            }
        }
        chain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.
     * User가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Optional<User> user = memberRepository.findById(authentication.getName()); // 유저를 유저명으로 찾습니다.
        //System.out.println("JwtAuthorizationFilter.getUsernamePasswordAuthenticationToken...authenticationToken : " +authentication );
        if(user!=null)
        {
            return authentication;
        }
        return null; // 유저가 없으면 NULL
    }

}