package hr.mywebshop.shoppingcart.dto;

/** DTO koji predstavlja jednu statistiku narudžbi.
 * @author ninov
 *
 */
public class OneStatDTO {
	
	private String name;
	private String action;
	private int count;
	
	public OneStatDTO() {}

	public OneStatDTO(String name, String action, int count) {
		super();
		this.name = name;
		this.action = action;
		this.count = count;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	

}
