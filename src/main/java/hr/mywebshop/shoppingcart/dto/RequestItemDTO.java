package hr.mywebshop.shoppingcart.dto;

import java.util.List;

/** DTO koji predstavlja zahtjev za dodavanje novog proizvoda.
 * @author ninov
 *
 */
public class RequestItemDTO {

	private String name;

	private String action;

	private List<PriceDTO> prices;
	
	public RequestItemDTO() {}
	
	

	public RequestItemDTO(String name, String action, List<PriceDTO> prices) {
		super();
		this.name = name;
		this.action = action;
		this.prices = prices;
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
