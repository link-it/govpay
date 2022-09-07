package it.govpay.pagopa.v2.causale;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class CausaleSemplice implements Causale {
	private String causale;

	@Override
	public String encode() throws UnsupportedEncodingException {
		if(this.causale == null) return null;
		return "01 " + Base64.encodeBase64String(this.causale.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String getSimple() throws UnsupportedEncodingException {
		return this.getCausale();
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getCausale() {
		return this.causale;
	}

	@Override
	public String toString() {
		return this.causale;
	}

}
