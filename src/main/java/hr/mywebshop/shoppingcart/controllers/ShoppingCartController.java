package hr.mywebshop.shoppingcart.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hr.mywebshop.shoppingcart.dto.CartDTO;
import hr.mywebshop.shoppingcart.dto.CartItemDTO;
import hr.mywebshop.shoppingcart.dto.OneStatDTO;
import hr.mywebshop.shoppingcart.dto.PriceDTO;
import hr.mywebshop.shoppingcart.dto.RequestItemDTO;
import hr.mywebshop.shoppingcart.dto.RequestNewUserDTO;
import hr.mywebshop.shoppingcart.dto.RequestStatDTO;
import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.services.ShoppingCartService;
import hr.mywebshop.shoppingcart.services.impl.BasicUsersService;

/** Upravljač zahtjevima za košaricu.
 * @author ninov
 *
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private BasicUsersService basicUsersService;
	
	
	/** Krajnja točka vraća sadržaj košarice za korisnika koji je vlasnik sjednice.
	 * @param idCustomer ID korisnika koji želi pogledati svoju košaricu
	 * @return Cart košarica koja pripada korisniku
	 */
	@GetMapping("/cart/{idCustomer}")
	public ResponseEntity<?> getCartContent(@PathVariable Long idCustomer, Principal principal) {
		
		if (idCustomer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");
		
		if (!shoppingCartService.isThisResourceOwnersSession(principal.getName(), idCustomer)) 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User: "+principal.getName() +" is not the owner of this resource!");

		

		long customerID = idCustomer.longValue();
		Customer customer = shoppingCartService.getCartUserFromRequest(customerID);

		if (customer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");

		CartDTO customerCart = shoppingCartService.getCartThatBelongsToUser(customer);

		if (customerCart == null) return ResponseEntity.notFound().build();

		return ResponseEntity.ok().body(customerCart);
	}

	
	/**  Krajnja točka koja dodaje proizvod u košaricu korisnika koji je vlasnik sjednice.
	 * @param idCustomer ID korisnika koji želi dodati predmet u košaricu
	 * @param requestItemDTO Proizvod koji se dodaje u košaricu
	 * @param principal Podaci o vlasniju sesije
	 * @return Proizvod koji je dodan u košaricu
	 */
	@PostMapping("/cart/{idCustomer}/items")
	public ResponseEntity<?> addItemToCart(@PathVariable Long idCustomer, @RequestBody RequestItemDTO requestItemDTO, Principal principal) {

		if (requestItemDTO == null) return ResponseEntity.badRequest().body("Bad Item object.");

		if (idCustomer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");
		long customerID = idCustomer.longValue();
		
		if (!shoppingCartService.isThisResourceOwnersSession(principal.getName(), idCustomer)) 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User: "+principal.getName() +" is not the owner of this resource!");
		
		Customer customer = shoppingCartService.getCartUserFromRequest(customerID);

		if (customer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");
		
		// Provjeri parametre
		if (requestItemDTO.getName() == null) return ResponseEntity.badRequest().body("Name is null!");
		if (requestItemDTO.getAction() == null) return ResponseEntity.badRequest().body("Action is null!");
		if (requestItemDTO.getPrices() == null) return ResponseEntity.badRequest().body("Prices are null!");
		if (requestItemDTO.getPrices().size() < 1) return ResponseEntity.badRequest().body("There must be at least 1 price!");
		
		for (PriceDTO tr : requestItemDTO.getPrices()) {
			if (tr.getRecurrences() == null) return ResponseEntity.badRequest().body("Recurrences are null!");
			if (tr.getValue() == null) return ResponseEntity.badRequest().body("Value is null!");
			if (tr.getValue().longValue() < 0)  return ResponseEntity.badRequest().body("Value is negative!");
		}

		CartItemDTO responseItem = shoppingCartService.addItemToUsersCart(customer, requestItemDTO);

		if (responseItem == null) return ResponseEntity.internalServerError().body("Failed to add Item to Cart.");
		
		URI lokacija = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(responseItem.getId()).toUri();
		return ResponseEntity.created(lokacija).body(responseItem);
	}


	
	
	/** Krajnja točka koja makne specifičan proizvod iz košarice korisnika koji je vlasnik sjednice.
	  * @param idCustomer ID korisnika koji želi izbrisati predmet
	 * @param idCartItem Proizvod koji se briše iz košarice
	 * @param principal Podaci o vlasniju sesije
	 * @return Izbrisani objekt iz košarice
	 */
	@DeleteMapping("/cart/{idCustomer}/items/{idCartItem}")
	public ResponseEntity<?> removeItemFromCart(@PathVariable Long idCustomer, @PathVariable Long idCartItem, Principal principal) {

		if (idCustomer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");

		if (idCartItem == null) return ResponseEntity.badRequest().body("Bad Cart Item ID.");
		
		if (!shoppingCartService.isThisResourceOwnersSession(principal.getName(), idCustomer)) 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User: "+principal.getName() +" is not the owner of this resource!");

		long customerID = idCustomer.longValue();
		Customer customer = shoppingCartService.getCartUserFromRequest(customerID);

		if (customer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");

		long cartItemID = idCartItem.longValue();

		CartItemDTO responseItem = shoppingCartService.removeItemFromUsersCart(customer, cartItemID);

		if (responseItem == null) return ResponseEntity.internalServerError().body("Failed to remove Item from Cart.");

		return ResponseEntity.ok().body(responseItem);

	}

	
	
	/**  Krajnja točka koja isprazni košaricu korisnika koji je vlasnik sjednice.
	 * @param idCustomer  ID korisnika koji želi izbrisati predmet
	 * @param principal Podaci o vlasniju sesije
	 * @return Košaricu s proizvodima koji su izbrisani
	 */
	@DeleteMapping("/cart/{idCustomer}")
	public ResponseEntity<?> evictTheCart(@PathVariable Long idCustomer, Principal principal) {

		if (idCustomer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");
		
		if (!shoppingCartService.isThisResourceOwnersSession(principal.getName(), idCustomer)) 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User: "+principal.getName() +" is not the owner of this resource!");

		long customerID = idCustomer.longValue();
		Customer customer = shoppingCartService.getCartUserFromRequest(customerID);
		
		if (customer == null) return ResponseEntity.badRequest().body("Bad Customer ID.");
		
		CartDTO customerCart = shoppingCartService.getCartThatBelongsToUser(customer);
		if (customerCart == null) return ResponseEntity.notFound().build();
		
		boolean result = shoppingCartService.removeAllItemsFromUsersCart(customer);
		if (result == false) return ResponseEntity.internalServerError().body("Failed to remove Items from Cart.");

		return ResponseEntity.ok().body(customerCart);
	}

	/** Krajnja točka koja vraća statistiku svih proizvoda.
	 * @return Lista filtriranih statistika
	 */
	
	@GetMapping("/cart/stats")
	public ResponseEntity<?> getCartStatisticsOnSales() {
		
		List<OneStatDTO> stats = shoppingCartService.getAllStatistics();
		return ResponseEntity.ok().body(stats);
	}
	
	/** Krajnja točka koja vraća filtriranu statistiku prema zahtjevima predanima u parametrima.
	 * @param requestStatDTO Pomoćni objekt koji definira zahtjeve filtriranja
	 * @return Lista filtriranih statistika
	 */
	
	@PostMapping("/cart/stats")
	public ResponseEntity<?> getSpecificCartStatisticsOnSales(@RequestBody RequestStatDTO requestStatDTO) {
		
		if (requestStatDTO == null) return ResponseEntity.badRequest().body("Bad request DTO."); 
		List<OneStatDTO> results = shoppingCartService.getStatsForItemAndActionInTime(requestStatDTO.getName(), requestStatDTO.getAction(), requestStatDTO.getStart(), requestStatDTO.getEnd());
		return ResponseEntity.ok().body(results);
		
	}
	
	/** Stvori novog korisnika u sustavu.
	 * @param requestNewUserDTO Podaci o korisniku
	 * @return Pohranjeni korisnik u bazi
	 */
	@PostMapping("/customers")
	public ResponseEntity<?> registerNewUser(@RequestBody RequestNewUserDTO requestNewUserDTO) {

		if (requestNewUserDTO == null) return ResponseEntity.badRequest().body("Request body is null!");

		if (requestNewUserDTO.getName() == null || requestNewUserDTO.getName().isBlank()) return ResponseEntity.badRequest().body("Bad Name.");
		if (requestNewUserDTO.getLastname() == null || requestNewUserDTO.getLastname().isBlank()) return ResponseEntity.badRequest().body("Bad Last Name.");
		if (requestNewUserDTO.getUsername() == null || requestNewUserDTO.getUsername().isBlank()) return ResponseEntity.badRequest().body("Bad Username.");
		if (requestNewUserDTO.getPassword() == null || requestNewUserDTO.getPassword().isBlank()) return ResponseEntity.badRequest().body("Bad Password.");

		Customer customer = basicUsersService.registerNewUser(requestNewUserDTO);
		if (customer == null) return ResponseEntity.badRequest().body("Username is taken!");

		URI lokacija = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(customer.getId()).toUri();
		return ResponseEntity.created(lokacija).body(customer);
		
	}



}
