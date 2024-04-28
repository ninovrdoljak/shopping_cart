package hr.mywebshop.shoppingcart;

import java.sql.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import hr.mywebshop.shoppingcart.models.CartItem;
import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.models.Price;
import hr.mywebshop.shoppingcart.repository.CartItemRepository;
import hr.mywebshop.shoppingcart.repository.CustomerRepository;
import hr.mywebshop.shoppingcart.repository.PriceRepository;

/** Glavna metoda aplikacije.
 * @author ninov
 *
 */
@SpringBootApplication
public class ShoppingcartApplication {
	
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private PriceRepository priceRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartApplication.class, args);
	}

	/** Metoda puni bazu početnim podacima.
	 * @param passwordEncoder objekt za kodiranje lozinki.
	 * @return InitializingBean
	 */
	@Bean
	InitializingBean initDatabase(PasswordEncoder passwordEncoder) {
		return () -> {

			customerRepository.deleteAll();
			Customer fristCustomer = customerRepository.save(new Customer("ivoIvic", "Ivo", "Ivić", passwordEncoder.encode("lozinka123456")));
			Customer secondCustomer = customerRepository.save(new Customer("markoMarkic", "Marko", "Markić", passwordEncoder.encode("lozinka789123")));
			
			cartItemRepository.deleteAll();
			CartItem tvScreen = cartItemRepository.save(new CartItem("2112 Cool TV Screen", "ADD", fristCustomer.getId(), new Date(System.currentTimeMillis())));
			CartItem tvScreen2 = cartItemRepository.save(new CartItem("2112 Cool TV Screen", "ADD", fristCustomer.getId(), new Date(System.currentTimeMillis())));
			CartItem tvRemote = cartItemRepository.save(new CartItem("2145 Cool TV Remote", "ADD", fristCustomer.getId(), new Date(System.currentTimeMillis())));
			CartItem cdPlayer = cartItemRepository.save(new CartItem("2155 CD Player", "MODIFY", fristCustomer.getId(), new Date(System.currentTimeMillis())));
			
			CartItem tvRemoteSecond = cartItemRepository.save(new CartItem("2145 Cool TV Remote", "ADD", secondCustomer.getId(), new Date(System.currentTimeMillis())));

			priceRepository.deleteAll();
			priceRepository.save(new Price("ONE_TIME", 800.0, tvScreen.getId()));
			priceRepository.save(new Price("ONE_TIME", 800.0, tvScreen2.getId()));
			
			priceRepository.save(new Price("12 months", 20.0, tvRemote.getId()));
			priceRepository.save(new Price("ONE_TIME", 20.0, tvRemote.getId()));
			
			priceRepository.save(new Price("ONE_TIME", 50.0, cdPlayer.getId()));
			
			priceRepository.save(new Price("12 months", 20.0, tvRemoteSecond.getId()));
			priceRepository.save(new Price("ONE_TIME", 20.0, tvRemoteSecond.getId()));

		};
	}

}
