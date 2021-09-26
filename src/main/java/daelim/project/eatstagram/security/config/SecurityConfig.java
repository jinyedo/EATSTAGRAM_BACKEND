package daelim.project.eatstagram.security.config;

import daelim.project.eatstagram.security.handler.LoginFailureHandler;
import daelim.project.eatstagram.security.service.AuthMemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthMemberDetailsService authMemberDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 로그인 실패 핸들러
    @Bean
    AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://www.whereyedo.com"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().configurationSource(corsConfigurationSource()); // cors 설정

        http.csrf().disable(); // CSRF 토큰을 발해하지 않도록 지정

        // 일반 로그인
        http.formLogin()
                .loginPage("/")
                .failureUrl("/loginFail") // 인가/인증에 문제시 로그인 화면으로 이동
                .defaultSuccessUrl("/loginSuccess", true)
                .failureHandler(loginFailureHandler());

        // 소셜 로그인
        http.oauth2Login()// 로그인 시에 OAuth 를 사용한 로그인이 가능하도록
                .loginPage("/")
                .failureUrl("/loginFail") // 인가/인증에 문제시 로그인 화면으로 이동
                .defaultSuccessUrl("/loginSuccess", true)
                .failureHandler(loginFailureHandler());

        // 자동 로그인 설정
        http.rememberMe()
                .key("autoLoginToken")
                .tokenValiditySeconds(60*60*7).userDetailsService(authMemberDetailsService) // 7일
                .rememberMeParameter("auto-login")
                .rememberMeCookieName("remember-me");

        // 로그아웃 설정
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 로그아웃 시 세션정보를 제거할 지 여부를 지정한다. 기본값은 true 이고 세션정보를 제거한다.
                .deleteCookies("JSESSIONID", "remember-me"); // 로그아웃 시 제거할 쿠키이름을 지정한다.
    }
}
