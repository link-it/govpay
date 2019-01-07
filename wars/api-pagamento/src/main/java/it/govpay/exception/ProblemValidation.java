package it.govpay.exception;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import it.govpay.pagamento.v2.beans.Problem;

public class ProblemValidation extends Problem {
	
	public ProblemValidation() {
		this.setType("https://govpay.readthedocs.io/it/latest/integrazione/99_errori.html#http-400-bad-request");
		this.setStatus(Status.BAD_REQUEST.getStatusCode());
		this.setTitle(Status.BAD_REQUEST.getReasonPhrase());
		this.setDetail("La richiesta non e' stata validata.");
		this.invalidParams = new ArrayList<ProblemValidation.InvalidParam>();
	}
	
	public class InvalidParam {
		private String name;
		private String reason;
		private String value;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	private List<InvalidParam> invalidParams;

	public List<InvalidParam> getInvalidParams() {
		return invalidParams;
	}

	public void setInvalidParams(List<InvalidParam> invalidParams) {
		this.invalidParams = invalidParams;
	}
	
	public void addInvalidParam(String name, String reason, Object value) {
		InvalidParam ip = new InvalidParam();
		ip.setName(name);
		ip.setReason(reason);
		ip.setValue(value != null ? value.toString() : null);
		this.invalidParams.add(ip);
	}
}
