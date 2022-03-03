package com.example.spring_bootstrap.configs;

import com.example.spring_bootstrap.service.UserDetailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final UserDetailServiceImp userDetailServiceImp;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailServiceImp userDetailServiceImp) {
        this.successUserHandler = successUserHandler;
        this.userDetailServiceImp = userDetailServiceImp;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceImp);
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/user/**").access("hasRole('ADMIN') or hasRole('USER')")
                //.antMatchers("/user/**").access("hasRole('ADMIN') and hasRole('USER')")
                //.antMatchers("/user/**").access("hasRole('USER')")
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .successHandler(successUserHandler)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}