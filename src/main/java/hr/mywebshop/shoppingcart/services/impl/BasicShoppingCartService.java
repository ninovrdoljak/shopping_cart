package hr.mywebshop.shoppingcart.services.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.mywebshop.shoppingcart.dto.CartDTO;
import hr.mywebshop.shoppingcart.dto.CartItemDTO;
import hr.mywebshop.shoppingcart.dto.OneStatDTO;
import hr.mywebshop.shoppingcart.dto.PriceDTO;
import hr.mywebshop.shoppingcart.dto.RequestItemDTO;
import hr.mywebshop.shoppingcart.models.CartItem;
import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.models.Price;
import hr.mywebshop.shoppingcart.repository.CartItemRepository;
import hr.mywebshop.shoppingcart.repository.CustomerRepository;
import hr.mywebshop.shoppingcart.repository.PriceRepository;
import hr.mywebshop.shoppingcart.services.ShoppingCartService;

/** Osnovna implementacija backend usluga.
 * @author ninov
 *
 */
@Service
public class BasicShoppingCartService implements ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PriceRepository priceRepository;

	
	@Override
	public Customer getCartUserFromRequest(long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		return customer.isPresent() ? customer.get() : null;
	}

	@Override
	public CartDTO getCartThatBelongsToUser(Customer customer) {

		if (customer == null) return null;
		if (customer.getId() == null) return null;

		long idCustomer = customer.getId().longValue();
		List<CartItem> listModels = cartItemRepository.findByIdCustomer(idCustomer);

		List<CartItemDTO> listItems = new ArrayList<>();
		for (CartItem model : listModels) {
			List<PriceDTO> prices = getPricesForItemInCart(model.getId());
			listItems.add(new CartItemDTO(model.getId(), model.getName(), model.getAction(), prices, model.getDatecreated()));
		}

		return new CartDTO(idCustomer, customer.getUsername(), listItems);
	}

	@Override
	public CartItemDTO addItemToUsersCart(Customer customer, RequestItemDTO item) {

		if (customer == null) return null;
		if (customer.getId() == null) return null;
		if (item == null) return null;

		long idCustomer = customer.getId().longValue();

		String itemName = item.getName();
		String itemAaction = item.getAction();
		List<PriceDTO> itemPrices = item.getPrices();

		CartItem newCartItem = new CartItem(itemName, itemAaction, idCustomer, new Date(System.currentTimeMillis()));

		CartItem saved = cartItemRepository.save(newCartItem);

		long itemId = saved.getId().longValue();

		for (PriceDTO p : itemPrices) {
			priceRepository.save(new Price(p.getRecurrences(), p.getValue(), itemId));
		}

		CartItemDTO response = new CartItemDTO(saved.getId(), saved.getName(), saved.getAction(), itemPrices, saved.getDatecreated());

		return response;
	}

	@Override
	public CartItemDTO removeItemFromUsersCart(Customer customer, long cartItemId) {

		if (customer == null) return null;
		if (customer.getId() == null) return null;
		long idCustomer = customer.getId().longValue();

		Optional<CartItem> cartItemForDeletion = cartItemRepository.findById(cartItemId);
		if (cartItemForDeletion.isEmpty()) return null;

		CartItem item = cartItemForDeletion.get();

		long itemCustomerID = item.getIdCustomer() == null ? -1 : item.getIdCustomer().longValue();
		if (idCustomer != itemCustomerID) return null;


		List<PriceDTO> prices = getPricesForItemInCart(item.getId());

		CartItemDTO response = new CartItemDTO(item.getId(), item.getName(), item.getAction(), prices, item.getDatecreated());

		deletePricesForItemInCart(item.getId());
		cartItemRepository.deleteById(item.getId());

		return response;
	}

	@Override
	public boolean removeAllItemsFromUsersCart(Customer customer) {

		if (customer == null) return false;
		if (customer.getId() == null) return false;

		List<CartItem> cartItemsList = cartItemRepository.findByIdCustomer(customer.getId());

		for (CartItem item : cartItemsList) {
			deletePricesForItemInCart(item.getId());
			cartItemRepository.deleteById(item.getId());
		}

		return true;
	}

	@Override
	public List<OneStatDTO> getAllStatistics() {

		List<CartItem> listItems = cartItemRepository.findAll();

		//List<String> uniqueNames = listItems.stream().map(item -> item.getName()).distinct().collect(Collectors.toList());
		
		List<String> allNames = new ArrayList<>();
		for (CartItem item : listItems) allNames.add(item.getName());
		
		Set<String> uniqueNames = new HashSet<>(allNames);
	
		List<OneStatDTO> listStats = new ArrayList<>();

		for (String name : uniqueNames) {
			List<CartItem> oneProduct = cartItemRepository.findByName(name);
			Map<String, Integer> map = new HashMap<>();

			for (CartItem product : oneProduct) {
				map.put(product.getAction(), map.get(product.getAction()) == null ? 1 : 1+map.get(product.getAction()));
			}


			for (Entry<String, Integer> ent : map.entrySet()) {
				listStats.add(new OneStatDTO(name, ent.getKey(), ent.getValue()));
			}

		}

		return listStats;
	}

	@Override
	public List<OneStatDTO> getStatsForItemAndActionInTime(String name, String action, Date start, Date end) {

		List<CartItem> filteredItems = new ArrayList<>();

		// ako je name == null, onda pregledavamo sve
		List<CartItem> listItems = (name == null ? cartItemRepository.findAll() : cartItemRepository.findByName(name));

		// ako je action == null, onda pregledavamo sve
		if (action == null) {
			for (CartItem item : listItems) filteredItems.add(item);
		} else {
			for (CartItem item : listItems) {
				if (item.getAction().toLowerCase().equals(action.toLowerCase())) {
					filteredItems.add(item);
				}
			} 
		}

		// ako je start date null, onda ne provjeravamo početni datum
		if (start != null) {
			filteredItems = filteredItems.stream().filter(item -> item.getDatecreated().after(start) || item.getDatecreated().equals(start)).collect(Collectors.toList());
		}

		// ako je end date null, onda ne provjeravamo završni datum
		if (end != null) {
			filteredItems = filteredItems.stream().filter(item -> item.getDatecreated().before(end) || item.getDatecreated().equals(end)).collect(Collectors.toList());
		}


		List<String> uniqueNames = filteredItems.stream().map(item -> item.getName()).distinct().collect(Collectors.toList());

		List<OneStatDTO> listStats = new ArrayList<>();

		for (String oneName : uniqueNames) {
			List<CartItem> oneProduct = filteredItems.stream().filter(item -> item.getName().equals(oneName)).collect(Collectors.toList());
			Map<String, Integer> map = new HashMap<>();

			for (CartItem product : oneProduct) {
				map.put(product.getAction(), map.get(product.getAction()) == null ? 1 : 1+map.get(product.getAction()));
			}


			for (Entry<String, Integer> ent : map.entrySet()) {
				listStats.add(new OneStatDTO(oneName, ent.getKey(), ent.getValue()));
			}

		}

		return listStats;
	}

	@Override
	public List<PriceDTO> getPricesForItemInCart(long cartItemId) {
		List<PriceDTO> result = new ArrayList<>();
		List<Price> listPrices = priceRepository.findByItemid(cartItemId);

		if (listPrices == null || listPrices.size() == 0) return result;

		for (Price t : listPrices) {
			result.add(new PriceDTO(t.getRecurrences(), t.getValue()));
		}

		return result;
	}

	@Override
	public void deletePricesForItemInCart(long cartItemId) {
		List<Price> listPrices = priceRepository.findByItemid(cartItemId);

		for (Price p : listPrices) {
			priceRepository.delete(p);
		}

	}

	@Override
	public boolean isThisResourceOwnersSession(String username, long customerId) {
		Optional<Customer> customer = customerRepository.findByUsername(username);
		if (customer.isEmpty()) return false;
		if (customer.get().getId() == null) return false;
		if (customer.get().getId().longValue() == customerId) return true;
		return false;
	}

}
