package hr.mywebshop.shoppingcart.services;

import java.sql.Date;
import java.util.List;

import hr.mywebshop.shoppingcart.dto.CartDTO;
import hr.mywebshop.shoppingcart.dto.CartItemDTO;
import hr.mywebshop.shoppingcart.dto.OneStatDTO;
import hr.mywebshop.shoppingcart.dto.PriceDTO;
import hr.mywebshop.shoppingcart.dto.RequestItemDTO;
import hr.mywebshop.shoppingcart.models.Customer;

/** Sučelje backend usluga u aplikaciji. Koristi ga upravljač zahtjeva i testne komponente.
 * @author ninov
 *
 */
public interface ShoppingCartService {
	
	/**  Dobij korisnika koji je vlasnik košarice kojom želimo upravljati.
	 * @param id Košarice
	 * @return Korisnik koji je vlasnik košarice
	 */
	Customer getCartUserFromRequest(long id);
	
	/** Dobij košaricu koja pripada korisniku.
	 * @param customer Korisnik koji šalje zahtjev
	 * @return Košarica koja pripada korisniku
	 */
	CartDTO getCartThatBelongsToUser(Customer customer);
	
	/** Dodaj proizvod u košaricu korisnika.
	 * @param customer Vlasnik košarice
	 * @param item Zahtjev koji sadrži podatke o novom proizvodu
	 * @return Spremljeni novi proizvod u košarici
	 */
	CartItemDTO addItemToUsersCart(Customer customer, RequestItemDTO item);
	
	/** Makni proizvod iz košarice korisnika.
	 * @param customer Korisnik koji je vlasnik košarice
	 * @param cartItemId ID proizvoda kojega mičemo iz košarice
	 * @return Maknuti proizvod
	 */
	CartItemDTO removeItemFromUsersCart(Customer customer, long cartItemId);
	
	/** Makni sve proizvode iz košarice korisnika.
	 * @param customer Korisnik koje prazni svoku košaricu
	 * @return USpješnost pražnjenja košarice
	 */
	boolean removeAllItemsFromUsersCart(Customer customer);
	
	/** Dobij statistiku za sve proizvode u košaricama.
	 * @return Ne filtrirana statistika
	 */
	List<OneStatDTO> getAllStatistics();
	
	/** Dobij filtriranu statistiku za proizvode u košaricama.
	 * @param name Ime proizvoda za koji želimo statistiku
	 * @param action Vrsta postupka koji radimo na proizvodu
	 * @param start Datum od kojeg filtriramo rezultate
	 * @param end Datum do kojeg filtriramo rezultate
	 * @return Filtrirana statistika
	 */
	List<OneStatDTO> getStatsForItemAndActionInTime(String name, String action, Date start, Date end);
	
	/** Dobij cijene za proizvod.
	 * @param cartItemId ID proizvoda čije cijene želimo dobiti
	 * @return Lista cijena koje ima proizvod
	 */
	List<PriceDTO> getPricesForItemInCart(long cartItemId);
	
	/** Izbriši cijene za proizvod.
	 * @param cartItemId ID proizvoda čije cijene brišemo
	 */
	void deletePricesForItemInCart(long cartItemId);
	
	/** Provjeri smije li vlasnik sesije koji šalje zahtjev upravljati resursom na kojeg je poslao zahtjev.
	 * @param username Korisničko ime vlasnika sesije
	 * @param customerId ID vlasnika resursa
	 * @return Vrijednost je li vlasnik sesije ujedno i vlasnik resursa
	 */
	boolean isThisResourceOwnersSession(String username, long customerId);

}
