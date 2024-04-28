package hr.mywebshop.shoppingcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.mywebshop.shoppingcart.models.Customer;

/** Sučelje za slanje naredbi na tablicu korisnika.
 * @author ninov
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	List<Customer> findAll();
	
	Optional<Customer> findById(long id);
	
	Optional<Customer> findByUsername(String username);

}
