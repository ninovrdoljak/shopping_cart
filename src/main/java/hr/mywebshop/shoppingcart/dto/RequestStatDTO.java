package hr.mywebshop.shoppingcart.dto;

import java.sql.Date;

/** DTO koji predstavlja zahtjev za filtriranje statistike.
 * @author ninov
 *
 */
public class RequestStatDTO {
	
	private String name;
	
	private String action;
	
	private Date start;
	
	private Date end;
	
	public RequestStatDTO() {}

	public RequestStatDTO(String name, String action, Date start, Date end) {
		super();
		this.name = name;
		this.action = action;
		this.start = start;
		this.end = end;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
}
