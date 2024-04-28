package hr.mywebshop.shoppingcart.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hr.mywebshop.shoppingcart.dto.RequestNewUserDTO;
import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.repository.CustomerRepository;
import hr.mywebshop.shoppingcart.services.UsersService;

@Service
public class BasicUsersService implements UsersService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Customer registerNewUser(RequestNewUserDTO newUser) {
		
		if (newUser == null) return null;
		
		if (newUser.getName() == null || newUser.getName().isBlank()) return null;
		if (newUser.getLastname() == null || newUser.getLastname().isBlank()) return null;
		if (newUser.getUsername() == null || newUser.getUsername().isBlank()) return null;
		if (newUser.getPassword() == null || newUser.getPassword().isBlank()) return null;
		
		Optional<Customer> existingUsername = customerRepository.findByUsername(newUser.getUsername());
		if (existingUsername.isPresent()) return null;
		
		Customer newCustomerFromDb = customerRepository.save(new Customer(newUser.getUsername(), newUser.getName(), newUser.getLastname(), passwordEncoder.encode(newUser.getPassword())));
		
		return newCustomerFromDb;
	}

}
