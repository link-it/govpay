package it.govpay.pagopa.v2.causale;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class CausaleSpezzoniStrutturati implements Causale {
	private List<String> spezzoni;
	private List<BigDecimal> importi;

	@Override
	public String encode() throws UnsupportedEncodingException {
		if(this.spezzoni == null) return null;
		String encoded = "03";
		for(int i=0; i<this.spezzoni.size(); i++) {
			encoded += " " + Base64.encodeBase64String(this.spezzoni.get(i).getBytes(StandardCharsets.UTF_8)) + " " + Base64.encodeBase64String(Double.toString(this.importi.get(i).doubleValue()).getBytes(StandardCharsets.UTF_8));
		}
		return encoded;
	}

	@Override
	public String getSimple() throws UnsupportedEncodingException {
		if(this.spezzoni != null && !this.spezzoni.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append(this.importi.get(0).doubleValue() + ": " + this.spezzoni.get(0) );
			return sb.toString();
		}

		return "";
	}

	public CausaleSpezzoniStrutturati() {
		this.spezzoni = new ArrayList<>();
		this.importi = new ArrayList<>();
	}

	public void setSpezzoni(List<String> spezzoni) {
		this.spezzoni = spezzoni;
	}

	public List<String> getSpezzoni() {
		return this.spezzoni;
	}

	public void setImporti(List<BigDecimal> importi) {
		this.importi = importi;
	}

	public List<BigDecimal> getImporti() {
		return this.importi;
	}

	public void addSpezzoneStrutturato(String spezzone, BigDecimal importo){
		this.spezzoni.add(spezzone);
		this.importi.add(importo);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<this.spezzoni.size(); i++) {
			sb.append(this.importi.get(i).doubleValue() + ": " + this.spezzoni.get(i) + "; ");
		}
		return sb.toString();
	}

}
