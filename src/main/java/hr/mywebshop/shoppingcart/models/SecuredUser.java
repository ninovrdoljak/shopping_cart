package hr.mywebshop.shoppingcart.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Objekt koji sadrži podatke o sigurnosti za korisnika koji šalje zahtjev.
 * @author ninov
 *
 */
public class SecuredUser implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SimpleGrantedAuthority> listRoles;
	
	private Long id;
	private String name;
	private String lastName;
	private String password;
	private String username;
	
	public SecuredUser(Customer customer) {
		this.id = customer.getId();
		this.name = customer.getName();
		this.lastName = customer.getLastname();
		this.password = customer.getPassword();
		this.username = customer.getUsername();
		
		this.listRoles = new ArrayList<>();
		listRoles.add(new SimpleGrantedAuthority("CUSTOMER"));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.listRoles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}
}
