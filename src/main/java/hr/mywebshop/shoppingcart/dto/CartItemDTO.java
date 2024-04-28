package hr.mywebshop.shoppingcart.dto;

import java.sql.Date;
import java.util.List;

/** DTO koji predstavlja proizvod u košarici korisnika.
 * @author ninov
 *
 */
public class CartItemDTO {

	private Long id;

	private String name;

	private String action;

	private List<PriceDTO> prices;
	
	private Date date;
	
	public CartItemDTO() {}

	public CartItemDTO(Long id, String name, String action, List<PriceDTO> prices, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.action = action;
		this.prices = prices;
		this.date = date;
	}
	
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<PriceDTO> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceDTO> prices) {
		this.prices = prices;
	}
	
	



}
