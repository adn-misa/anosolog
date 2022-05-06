/*
 * Author : AdNovum Informatik AG
 */

package com.adnovum.anosolog.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/assets/*").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.loginPage("/login.html")
				.permitAll();
	}
}
