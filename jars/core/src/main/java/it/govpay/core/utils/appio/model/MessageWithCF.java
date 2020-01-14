package it.govpay.core.utils.appio.model;

public class MessageWithCF {

	private Integer time_to_live;
	private MessageContent content;
	private DefaultAddresses default_addresses;
	private String fiscal_code;
	
	public Integer getTime_to_live() {
		return time_to_live;
	}
	public void setTime_to_live(Integer time_to_live) {
		this.time_to_live = time_to_live;
	}
	public MessageContent getContent() {
		return content;
	}
	public void setContent(MessageContent content) {
		this.content = content;
	}
	public DefaultAddresses getDefault_addresses() {
		return default_addresses;
	}
	public void setDefault_addresses(DefaultAddresses default_addresses) {
		this.default_addresses = default_addresses;
	}
	public String getFiscal_code() {
		return fiscal_code;
	}
	public void setFiscal_code(String fiscal_code) {
		this.fiscal_code = fiscal_code;
	}
	
	
}
