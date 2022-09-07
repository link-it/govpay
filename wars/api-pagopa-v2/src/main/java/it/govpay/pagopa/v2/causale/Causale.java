package it.govpay.pagopa.v2.causale;

import java.io.UnsupportedEncodingException;

public interface Causale {
	public String encode() throws UnsupportedEncodingException;
	public String getSimple() throws UnsupportedEncodingException;
}