package hr.mywebshop.shoppingcart.services;

import hr.mywebshop.shoppingcart.dto.RequestNewUserDTO;
import hr.mywebshop.shoppingcart.models.Customer;

/** Sučelje koje upravlja korisnicima u sustavu.
 * @author ninov
 *
 */
public interface UsersService {
	
	/** Registriraj novog korisnika u sustav.
	 * @param newUser Podaci o korisniku
	 * @return Spremljeni podaci o korisniku iz baze
	 */
	Customer registerNewUser(RequestNewUserDTO newUser);
	
}
