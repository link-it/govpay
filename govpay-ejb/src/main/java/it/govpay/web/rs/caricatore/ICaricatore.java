package it.govpay.web.rs.caricatore;

import it.govpay.bd.BasicBD;
import it.govpay.model.Applicazione;

import java.io.InputStream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

public interface ICaricatore {

	public String caricaVersamento(InputStream is, UriInfo uriInfo, HttpHeaders httpHeaders, BasicBD bd, Applicazione applicazioneAutenticata) throws Exception;
}
