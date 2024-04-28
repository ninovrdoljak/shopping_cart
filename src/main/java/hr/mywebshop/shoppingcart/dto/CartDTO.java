package hr.mywebshop.shoppingcart.dto;

import java.util.List;

/** DTO koji predstavlja košaricu korisnika.
 * @author ninov
 *
 */
public class CartDTO {
	
	private long id;
	
	private String customerName;
	
	private List<CartItemDTO> items;
	
	public CartDTO() {}

	public CartDTO(long id, String customerName, List<CartItemDTO> items) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}
	
	
	
	

}
