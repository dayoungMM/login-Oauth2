package com.moondy.loginoauth2.auth.config;

import com.moondy.loginoauth2.auth.domain.Role;
import com.moondy.loginoauth2.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final CustomOAuth2UserService customOAuth2UserService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .headers().frameOptions().disable()
//            .and()
//            .authorizeRequests()//url별 관리 설정 옵션 시작점
//            .antMatchers("/", "/css/**", "/images/**",
//                "/js/**", "/h2-console/**").permitAll() //항상 허용
////            .antMatchers("/api/v1/**").hasRole(Role.
////                                                   USER.name()) //role마다 접근하는 api 다르게 할려고 할 때
//            .anyRequest().authenticated() // 나머지는 인증 필요
//            .and()
//            .logout()
//            .logoutSuccessUrl("/") //로그아웃시 주소로 이동
//            .and()
//            .oauth2Login() // OAuth2 로그인 기능에 대한 설정의 진입점
//            .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보 가져올 때 설정
//            .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속조치 실행할 서비스. 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행할 부분
////            .httpBasic(withDefaults());
//        return http.build();
//    }

    //WebSecurityConfigurerAdapter 를 상속받아 사용할 때. deprecated 되어서 위의 코드로 수정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests()
            .antMatchers("/", "/css/**", "/images/**",
                "/js/**", "/h2-console/**").permitAll()
            .antMatchers("/api/v1/**").hasRole(Role.
                                                   USER.name())
            .anyRequest().authenticated()
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService);
    }
}