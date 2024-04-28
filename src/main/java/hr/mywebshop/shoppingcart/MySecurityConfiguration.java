package hr.mywebshop.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import hr.mywebshop.shoppingcart.services.impl.MyUserDetailsService;

/** Postavke sigurnosti u lancu filtriranja zahtjeva i koder lozinke.
 * @author ninov
 *
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class MySecurityConfiguration {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	private static final String[] AUTH_WHITELIST = {
	        "/api/cart/stats",
	        "/api/customers"
	};
	
	@SuppressWarnings({ "deprecation", "removal" })
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeRequests(auth -> auth
						.requestMatchers(AUTH_WHITELIST).permitAll()
						
						.anyRequest().authenticated())
				.userDetailsService(myUserDetailsService)
				.headers(headers -> headers.frameOptions().sameOrigin())
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.build();
		
		// CORS je disabled zbog Postman testiranja
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
