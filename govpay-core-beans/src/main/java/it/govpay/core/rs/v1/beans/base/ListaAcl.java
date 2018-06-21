package it.govpay.core.rs.v1.beans.base;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.utils.SimpleDateFormatUtils;

@org.codehaus.jackson.annotate.JsonPropertyOrder({
"acl"
})

public class ListaAcl extends JSONSerializable {
	
	@JsonProperty("acl")
	private List<AclPost> acl;
	public ListaAcl() {
		super();
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
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
		return acl;
	}

	public void setAcl(List<AclPost> acl) {
		this.acl = acl;
	}
}
