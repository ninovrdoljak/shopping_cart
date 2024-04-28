package hr.mywebshop.shoppingcart.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.models.SecuredUser;
import hr.mywebshop.shoppingcart.repository.CustomerRepository;


/** Implemnentacija usluge za upravljanje sigurnim korisnicima.
 * @author ninov
 *
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Pronađi korisnika iz baze podataka.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Customer> customer = customerRepository.findByUsername(username);
		if (customer.isEmpty()) throw new UsernameNotFoundException("No User with username: "+username);
		return new SecuredUser(customer.get());
	}

}
