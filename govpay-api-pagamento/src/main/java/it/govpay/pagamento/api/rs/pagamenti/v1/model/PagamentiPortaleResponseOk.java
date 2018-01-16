package it.govpay.pagamento.api.rs.pagamenti.v1.model;

public class PagamentiPortaleResponseOk {

	private long id;
	private String location;
	private String redirect;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRedirect() {
		return redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	
}
