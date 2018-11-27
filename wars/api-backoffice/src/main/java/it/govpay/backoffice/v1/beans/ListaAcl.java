package it.govpay.backoffice.v1.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"acl"
})

public class ListaAcl extends JSONSerializable {
	
	@JsonProperty("acl")
	private List<AclPost> acl;
	public ListaAcl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see it.govpay.core.rs.v1.beans.JSONSerializable#getJsonIdFilter()
	 */
	@Override
	public String getJsonIdFilter() {
		return "acl";
	}

	@JsonProperty("acl")
	public List<AclPost> getAcl() {
		return this.acl;
	}

	public void setAcl(List<AclPost> acl) {
		this.acl = acl;
	}
}
