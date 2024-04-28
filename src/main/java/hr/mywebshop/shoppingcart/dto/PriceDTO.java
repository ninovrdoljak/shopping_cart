package hr.mywebshop.shoppingcart.dto;

/** DTO koji predstavlja jedan objekt cijene proizvoda.
 * @author ninov
 *
 */
public class PriceDTO {
	
	
	private String recurrences;

	private Double value;
	
	public PriceDTO() {}
	
	

	public PriceDTO(String recurrences, Double value) {
		super();
		this.recurrences = recurrences;
		this.value = value;
	}



	public String getRecurrences() {
		return recurrences;
	}

	public void setRecurrences(String recurrences) {
		this.recurrences = recurrences;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
	
	

}
