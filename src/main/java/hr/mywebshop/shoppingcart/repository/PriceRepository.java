package hr.mywebshop.shoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.mywebshop.shoppingcart.models.Price;

/** Sučelje za slanje naredbi na tablicu cijena.
 * @author ninov
 *
 */
public interface PriceRepository extends JpaRepository<Price, Long> {
	
	List<Price> findByItemid(Long itemid);
	
}
