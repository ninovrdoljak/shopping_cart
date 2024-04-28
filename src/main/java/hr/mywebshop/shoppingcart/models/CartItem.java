package hr.mywebshop.shoppingcart.models;

import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Tablica proizvoda u košaricama korisnika.
 * @author ninov
 *
 */
@Entity
@Table(name = "cartitem")
public class CartItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/*@Column(name = "identifier")
	private Long identifier;*/
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "idcustomer")
	private Long idCustomer;
	
	@Column(name = "datecreated")
    private Date datecreated;
	
	public CartItem() {}

	public CartItem(String name, String action, Long idCustomer, Date datecreated) {
		super();
		
		this.name = name;
		this.action = action;
		this.idCustomer = idCustomer;
		this.datecreated = datecreated;
	}
	
	public Long getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}

	public Long getId() {
		return id;
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

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	
	
	
	

}
