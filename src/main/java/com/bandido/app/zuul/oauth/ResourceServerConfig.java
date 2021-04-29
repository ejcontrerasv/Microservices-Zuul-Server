package com.bandido.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
		}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/app/security/oauth/**")
		.permitAll()
		.antMatchers(HttpMethod.GET,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.HEAD,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.OPTIONS,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.POST,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.access("hasIpAddress('192.168.1.117') and hasRole('ADMIN')")
		.antMatchers(HttpMethod.PUT,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.access("hasIpAddress('192.168.1.117') and hasRole('ADMIN')")
		.antMatchers(HttpMethod.PATCH,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.access("hasIpAddress('192.168.1.117') and hasRole('ADMIN')")
		.antMatchers(HttpMethod.DELETE,"/app/documents/**","/app/status/**","/app/documents/type/**","/app/users/**")
		.access("hasIpAddress('192.168.1.117') and hasRole('ADMIN')")
		.anyRequest().authenticated()
		.and()
		.cors()
		.configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowedOrigins(Arrays.asList("*"));
		cors.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		cors.setAllowCredentials(true);
		cors.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);
		
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
//		tokenConverter.setSigningKey(jwtKey);
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}
	

}