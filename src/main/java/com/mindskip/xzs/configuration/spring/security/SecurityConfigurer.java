package com.mindskip.xzs.configuration.spring.security;

import com.mindskip.xzs.configuration.property.CookieConfig;
import com.mindskip.xzs.configuration.property.SystemConfig;
import com.mindskip.xzs.domain.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version 3.5.0
 * @description: The type Security configurer.
 *               Copyright (C), 2020-2025, 武汉思维跳跃科技有限公司
 * @date 2021/12/25 9:45
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    /**
     * The type Form login web security configurer adapter.
     */
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final SystemConfig systemConfig;
        private final LoginAuthenticationEntryPoint restAuthenticationEntryPoint;
        private final RestAuthenticationProvider restAuthenticationProvider;
        private final RestDetailsServiceImpl formDetailsService;
        private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
        private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;
        private final RestLogoutSuccessHandler restLogoutSuccessHandler;
        private final RestAccessDeniedHandler restAccessDeniedHandler;

        /**
         * Instantiates a new Form login web security configurer adapter.
         *
         * @param systemConfig                     the system config
         * @param restAuthenticationEntryPoint     the rest authentication entry point
         * @param restAuthenticationProvider       the rest authentication provider
         * @param formDetailsService               the form details service
         * @param restAuthenticationSuccessHandler the rest authentication success
         *                                         handler
         * @param restAuthenticationFailureHandler the rest authentication failure
         *                                         handler
         * @param restLogoutSuccessHandler         the rest logout success handler
         * @param restAccessDeniedHandler          the rest access denied handler
         */
        @Autowired
        public FormLoginWebSecurityConfigurerAdapter(SystemConfig systemConfig,
                LoginAuthenticationEntryPoint restAuthenticationEntryPoint,
                RestAuthenticationProvider restAuthenticationProvider, RestDetailsServiceImpl formDetailsService,
                RestAuthenticationSuccessHandler restAuthenticationSuccessHandler,
                RestAuthenticationFailureHandler restAuthenticationFailureHandler,
                RestLogoutSuccessHandler restLogoutSuccessHandler, RestAccessDeniedHandler restAccessDeniedHandler) {
            this.systemConfig = systemConfig;
            this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
            this.restAuthenticationProvider = restAuthenticationProvider;
            this.formDetailsService = formDetailsService;
            this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
            this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
            this.restLogoutSuccessHandler = restLogoutSuccessHandler;
            this.restAccessDeniedHandler = restAccessDeniedHandler;
        }

        /**
         * @param http http
         * @throws Exception exception
         *                   csrf is the from submit get method
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.headers().frameOptions().disable();

            List<String> securityIgnoreUrls = systemConfig.getSecurityIgnoreUrls();
            String[] ignores = new String[securityIgnoreUrls.size()];
            http
                    .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                    .and().authenticationProvider(restAuthenticationProvider)
                    .authorizeRequests()
                    .antMatchers(securityIgnoreUrls.toArray(ignores)).permitAll()
                    .antMatchers(org.springframework.http.HttpMethod.GET, "/api/teacher/resource/course/**")
                    .hasAnyRole(RoleEnum.TEACHER.getName(), RoleEnum.STUDENT.getName(), RoleEnum.ADMIN.getName())
                    .antMatchers("/api/admin/**").hasRole(RoleEnum.ADMIN.getName())
                    .antMatchers("/api/student/**").hasRole(RoleEnum.STUDENT.getName())
                    .antMatchers("/api/teacher/**").hasRole(RoleEnum.TEACHER.getName())
                    .anyRequest().permitAll()
                    .and().exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                    .and().formLogin().successHandler(restAuthenticationSuccessHandler)
                    .failureHandler(restAuthenticationFailureHandler)
                    .and().logout().logoutUrl("/api/user/logout").logoutSuccessHandler(restLogoutSuccessHandler)
                    .invalidateHttpSession(true)
                    .and().rememberMe().key(CookieConfig.getName()).tokenValiditySeconds(CookieConfig.getInterval())
                    .userDetailsService(formDetailsService)
                    .and().csrf().disable()
                    .cors();
        }

        /**
         * Cors configuration source cors configuration source.
         *
         * @return the cors configuration source
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            final CorsConfiguration configuration = new CorsConfiguration();
            configuration.setMaxAge(3600L);
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:8002", "http://localhost:8003"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/api/**", configuration);
            return source;
        }

        /**
         * Authentication filter rest login authentication filter.
         *
         * @return the rest login authentication filter
         * @throws Exception the exception
         */
        @Bean
        public RestLoginAuthenticationFilter authenticationFilter() throws Exception {
            RestLoginAuthenticationFilter authenticationFilter = new RestLoginAuthenticationFilter();
            authenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            authenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
            authenticationFilter.setAuthenticationManager(authenticationManagerBean());
            authenticationFilter.setUserDetailsService(formDetailsService);
            return authenticationFilter;
        }

    }
}
