package it.govpay.pagopa.v2.causale;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class CausaleSpezzoni implements Causale {
	private List<String> spezzoni;

	@Override
	public String encode() throws UnsupportedEncodingException {
		if(this.spezzoni == null) return null;
		String encoded = "02";
		for(String spezzone : this.spezzoni) {
			encoded += " " + Base64.encodeBase64String(spezzone.getBytes(StandardCharsets.UTF_8));
		}
		return encoded;
	}

	@Override
	public String getSimple() throws UnsupportedEncodingException {
		if(this.spezzoni != null && !this.spezzoni.isEmpty())
			return this.spezzoni.get(0);

		return "";
	}

	public void setSpezzoni(List<String> spezzoni) {
		this.spezzoni = spezzoni;
	}

	public List<String> getSpezzoni() {
		return this.spezzoni;
	}

	@Override
	public String toString() {
		return StringUtils.join(this.spezzoni, "; ");
	}

}
