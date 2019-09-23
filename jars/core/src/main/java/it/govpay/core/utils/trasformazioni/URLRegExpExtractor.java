package it.govpay.core.utils.trasformazioni;

import java.util.List;

import org.openspcoop2.utils.regexp.RegExpException;
import org.openspcoop2.utils.regexp.RegExpNotFoundException;
import org.openspcoop2.utils.regexp.RegularExpressionEngine;
import org.slf4j.Logger;

import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class URLRegExpExtractor {

	private Logger log;

	private String url;

	public URLRegExpExtractor(String url, Logger log) {
		this.url = url;
		this.log = log;
	}

	public boolean match(String pattern) throws TrasformazioneException {
		String v = read(pattern);
		return v!=null && !"".equals(v);
	}

	public String read(String pattern) throws TrasformazioneException {
		String valore = null;
		try {
			valore = RegularExpressionEngine.getStringMatchPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}

	public List<String> readList(String pattern) throws TrasformazioneException {
		List<String> valore = null;
		try {
			valore = RegularExpressionEngine.getAllStringMatchPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}



	public boolean found(String pattern) throws TrasformazioneException {
		String v = find(pattern);
		return v!=null && !"".equals(v);
	}

	public String find(String pattern) throws TrasformazioneException {
		String valore = null;
		try {
			valore = RegularExpressionEngine.getStringFindPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}

	public List<String> findAll(String pattern) throws TrasformazioneException {
		List<String> valore = null;
		try {
			valore = RegularExpressionEngine.getAllStringFindPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}
}
