package hr.mywebshop.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.mywebshop.shoppingcart.models.CartItem;

/** Sučelje za slanje naredbi na tablicu proizvoda.
 * @author ninov
 *
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findAll();
	
	List<CartItem> findByName(String name);
	
	List<CartItem> findByIdCustomer(Long idCustomer);

}
