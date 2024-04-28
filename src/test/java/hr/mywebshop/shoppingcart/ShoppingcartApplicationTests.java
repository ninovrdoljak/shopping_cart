package hr.mywebshop.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import hr.mywebshop.shoppingcart.controllers.ShoppingCartController;
import hr.mywebshop.shoppingcart.dto.OneStatDTO;
import hr.mywebshop.shoppingcart.dto.PriceDTO;
import hr.mywebshop.shoppingcart.dto.RequestItemDTO;
import hr.mywebshop.shoppingcart.dto.RequestStatDTO;
import hr.mywebshop.shoppingcart.models.CartItem;
import hr.mywebshop.shoppingcart.models.Customer;
import hr.mywebshop.shoppingcart.repository.CartItemRepository;
import hr.mywebshop.shoppingcart.repository.CustomerRepository;
import hr.mywebshop.shoppingcart.repository.PriceRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShoppingcartApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ShoppingCartController cartController;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PriceRepository priceRepository;
	

	@Test
	@Order(1) 
	void contextLoads() {
		assertNotNull(cartController);
		assertNotNull(cartItemRepository);
		assertNotNull(customerRepository);
		assertNotNull(priceRepository);
	}

	@Test
	@Order(2) 
	void databaseIsNotEmpty() {

		if (cartItemRepository.findAll().size() == 0) {
			fail("Cart DB is empty!");
		}
		if (customerRepository.findAll().size() == 0) {
			fail("Customer DB is empty!");
		}
		if (priceRepository.findAll().size() == 0) {
			fail("Prices DB is empty!");
		}
	}

	@Test
	@Order(3) 
	void tryToGetAcessToStats() {
		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.get("/api/cart/stats")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		} catch (Exception e) {
			fail("Error");
		}

	}

	@Test
	@Order(4) 
	void tryToGetAcessCartWithoutAuthorization() {

		List<Customer> customers = customerRepository.findAll();
		if (customers == null || customers.size() == 0) fail("No customers.");
		long oneId = customers.get(0).getId().longValue();
		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.get("/api/cart/"+oneId)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		} catch (Exception e) {
			fail("Error");
		}
	}

	@Test
	@Order(5) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void tryToGetAcessCartWithAuthorization() {

		Optional<Customer> custOpt = customerRepository.findByUsername("ivoIvic");
		if (custOpt.isEmpty()) fail("No Customer in DB");
		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.get("/api/cart/"+custOpt.get().getId().longValue())
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		} catch (Exception e) {
			fail("Error");
		}
	}

	@Test
	@Order(6) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void tryToGetAcessCartForWrongUser() {

		Optional<Customer> custOpt = customerRepository.findByUsername("markoMarkic");
		if (custOpt.isEmpty()) fail("No Customer in DB");
		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.get("/api/cart/"+custOpt.get().getId().longValue())
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		} catch (Exception e) {
			fail("Error");
		}
	}

	@Test
	@Order(7) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void addNewItemToUsersCart() {

		Optional<Customer> custOpt = customerRepository.findByUsername("ivoIvic");
		if (custOpt.isEmpty()) fail("No Customer in DB");

		Gson gson = new Gson();

		List<PriceDTO> prices = new ArrayList<>();
		prices.add(new PriceDTO("ONE_TIME", 10.0));
		prices.add(new PriceDTO("7 days", 5.0));

		String request = gson.toJson(new RequestItemDTO("1141 Green Hat", "ADD", prices));

		List<CartItem> hatsBefore = cartItemRepository.findByName("1141 Green Hat");

		try {
			
			this.mockMvc.perform( MockMvcRequestBuilders
					.post("/api/cart/"+custOpt.get().getId().longValue()+"/items")
					.with(csrf())
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
			.andExpect(status().isCreated());
			
			

		} catch (Exception e) {
			fail("Error");
		}

		List<CartItem> hatsAfter = cartItemRepository.findByName("1141 Green Hat");

		assertEquals(hatsBefore.size() + 1, hatsAfter.size());
		//assertEquals(hatsAfter.get(0).getIdCustomer(), custOpt.get().getId());
	}

	@Test
	@Order(8) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void failToAddNewItemToWrongCart() {
		Optional<Customer> custOpt = customerRepository.findByUsername("markoMarkic");
		if (custOpt.isEmpty()) fail("No Customer in DB");

		Gson gson = new Gson();

		List<PriceDTO> prices = new ArrayList<>();
		prices.add(new PriceDTO("ONE_TIME", 10.0));
		prices.add(new PriceDTO("7 days", 5.0));

		String request = gson.toJson(new RequestItemDTO("1141 Green Hat", "ADD", prices));

		List<CartItem> beforeHats = cartItemRepository.findByName("1141 Green Hat");

		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.post("/api/cart/"+custOpt.get().getId().longValue()+"/items")
					.with(csrf())
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());

		} catch (Exception e) {
			fail("Error");
		}

		List<CartItem> afterHats = cartItemRepository.findByName("1141 Green Hat");

		assertEquals(beforeHats.size(), afterHats.size());
	}

	@Test
	@Order(9) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void removeItemFromCart() {

		Optional<Customer> custOpt = customerRepository.findByUsername("ivoIvic");
		if (custOpt.isEmpty()) fail("No Customer in DB");

		List<CartItem> beforeIvoItems = cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());
		
		assertTrue(beforeIvoItems != null && beforeIvoItems.size() > 0);
		
		CartItem deletionItem = beforeIvoItems.get(0);

		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.delete("/api/cart/"+custOpt.get().getId().longValue()+"/items/"+deletionItem.getId())
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		} catch (Exception e) {
			fail("Error");
		}

		List<CartItem> afterIvoItems =  cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());

		assertEquals(beforeIvoItems.size(), afterIvoItems.size()+1);
		
		assertFalse(afterIvoItems.contains(deletionItem));

	}

	@Test
	@Order(10) 
	@WithMockUser(username = "markoMarkic", password = "lozinka789123", roles = "CUSTOMER")
	void removeAllItemsFromCart() {
		Optional<Customer> custOpt = customerRepository.findByUsername("markoMarkic");
		if (custOpt.isEmpty()) fail("No Customer in DB");

		List<CartItem> beforeItems = cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());
		
		assertTrue(beforeItems != null && beforeItems.size() > 0);
		
		

		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.delete("/api/cart/"+custOpt.get().getId().longValue())
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		} catch (Exception e) {
			fail("Error");
		}

		List<CartItem> afterItems =  cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());

		assertEquals(afterItems.size(), 0);
		
		for (CartItem beforeItem : beforeItems) {
			assertFalse(afterItems.contains(beforeItem));
		}
		
	}

	@Test
	@Order(11) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void failToPutPricesInNewItemToCart() {
		Optional<Customer> custOpt = customerRepository.findByUsername("ivoIvic");
		if (custOpt.isEmpty()) fail("No Customer in DB");

		Gson gson = new Gson();

		List<PriceDTO> prices = new ArrayList<>();
		
		String request = gson.toJson(new RequestItemDTO("1143 Item Without Prices", "ADD", prices));

		List<CartItem> itemsBefore = cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());

		try {
			this.mockMvc.perform( MockMvcRequestBuilders
					.post("/api/cart/"+custOpt.get().getId().longValue()+"/items")
					.with(csrf())
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		} catch (Exception e) {
			fail("Error");
		}

		List<CartItem> itemsAfter = cartItemRepository.findByIdCustomer(custOpt.get().getId().longValue());

		assertEquals(itemsBefore.size(), itemsAfter.size());

	}

	@Test
	@Order(12) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void getStatsForSpecificItem() {
		
		Gson gson = new Gson();

		// Tražimo statistiku za daljinski upravljač, sve naredbe, neograničeni vremnski period
		String request = gson.toJson(new RequestStatDTO("2145 Cool TV Remote", null, null, null));
		
		
		List<CartItem> items = cartItemRepository.findByName("2145 Cool TV Remote");
		
		try {
			MvcResult result = this.mockMvc.perform( MockMvcRequestBuilders
					.post("/api/cart/stats")
					.with(csrf())
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
			.andExpect(status().isOk()).andReturn();
			
			
			String content = result.getResponse().getContentAsString();
			
			Type listType = new TypeToken<ArrayList<OneStatDTO>>(){}.getType();
			List<OneStatDTO> listStats = gson.fromJson(content, listType);
			
			assertTrue(listStats.size() > 0);
			int resCount = 0;
			for (OneStatDTO tr : listStats) {
				resCount+=tr.getCount();
			}
			assertEquals(items.size(), resCount);
		} catch (Exception e) {
			fail("Error");
		}

	}

	@Test
	@Order(13) 
	@WithMockUser(username = "ivoIvic", password = "lozinka123456", roles = "CUSTOMER")
	void getStatsForSpecificItemByType() {
		Gson gson = new Gson();
		

		// Tražimo statistiku za daljinski upravljač, naredbe ADD, u vremenu u kojemu nema proizvoda
		String request = gson.toJson(new RequestStatDTO("2145 Cool TV Remote", "ADD", null, null));
		
		List<CartItem> items = cartItemRepository.findByName("2145 Cool TV Remote");
		
		List<CartItem> filtriraniItems = new ArrayList<>();
		for (CartItem item : items) {
			if (item.getAction().equals("ADD")) {
				filtriraniItems.add(item);
			}
		}
		
		try {
			MvcResult result = this.mockMvc.perform( MockMvcRequestBuilders
					.post("/api/cart/stats")
					.with(csrf())
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
			.andExpect(status().isOk()).andReturn();
			
			
			String content = result.getResponse().getContentAsString();
			
			Type listType = new TypeToken<ArrayList<OneStatDTO>>(){}.getType();
			List<OneStatDTO> listStats = gson.fromJson(content, listType);
			
			assertTrue(listStats.size() > 0);
			int resCount = 0;
			for (OneStatDTO tr : listStats) {
				if (tr.getAction().equals("ADD")) resCount+=tr.getCount();
				
			}
			assertEquals(filtriraniItems.size(), resCount);
		} catch (Exception e) {
			fail("Error");
		}

	}


}
