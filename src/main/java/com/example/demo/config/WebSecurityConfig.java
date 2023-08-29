package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.service.UserDetailsServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   private final UserDetailsServiceImpl userDetailsService;

   public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
      this.userDetailsService = userDetailsService;
   }

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }

   protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
         .authorizeRequests()
            .antMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
            .antMatchers("/admin/product").access("hasRole('ROLE_ADMIN')")
            .antMatchers("/signup").permitAll()
            .and()
         .exceptionHandling()
            .accessDeniedPage("/403")
            .and()
         .formLogin()
            .loginProcessingUrl("/j_spring_security_check")
            .loginPage("/admin/login")
            .defaultSuccessUrl("/productList")
            .failureUrl("/admin/login?error=true")
            .usernameParameter("userName")
            .passwordParameter("password")
            .and()
         .logout()
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/");
   }
}
