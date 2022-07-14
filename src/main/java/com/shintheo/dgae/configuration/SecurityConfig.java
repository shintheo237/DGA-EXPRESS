package com.shintheo.dgae.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shintheo.dgae.filters.JwtAuthenticationFilter;
import com.shintheo.dgae.filters.JwtAuthorizationFilter;
import com.shintheo.dgae.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsServiceImpl userDetailsServiceImpl;

	public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
		super();
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/**", "/css/**","/js/**", "/refreshToken/**", "/announcements/**","/search/{departuredate}/{departuretown}/announcements/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/signup").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/v3/api-docs/**",
                "/swagger-ui/**", "/swagger-ui/index.html/**");
    }

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
