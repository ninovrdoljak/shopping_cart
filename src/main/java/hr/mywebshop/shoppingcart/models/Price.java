package hr.mywebshop.shoppingcart.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Tablica cijena proizvoda u košaricama korisnika.
 * @author ninov
 *
 */
@Entity
@Table(name = "price")
public class Price {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "recurrences")
	private String recurrences;
	
	@Column(name = "value")
	private Double value;
	
	@Column(name = "itemid")
	private Long itemid;
	
	public Price() {}

	public Price(String recurrences, Double value, Long itemid) {
		super();
		this.recurrences = recurrences;
		this.value = value;
		this.itemid = itemid;
	}

	public Long getId() {
		return id;
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

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	
}
