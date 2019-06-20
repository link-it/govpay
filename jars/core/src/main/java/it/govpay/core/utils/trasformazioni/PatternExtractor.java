package it.govpay.core.utils.trasformazioni;

import java.util.List;

import org.openspcoop2.utils.json.JsonPathExpressionEngine;
import org.openspcoop2.utils.json.JsonPathNotFoundException;
import org.openspcoop2.utils.json.JsonPathNotValidException;
import org.openspcoop2.utils.xml.AbstractXPathExpressionEngine;
import org.openspcoop2.utils.xml.DynamicNamespaceContext;
import org.openspcoop2.utils.xml.XMLUtils;
import org.openspcoop2.utils.xml.XPathNotFoundException;
import org.openspcoop2.utils.xml.XPathNotValidException;
import org.openspcoop2.utils.xml.XPathReturnType;
import org.slf4j.Logger;
import org.w3c.dom.Element;

import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class PatternExtractor {

	private Logger log;

	private Element element = null;
	private Boolean refresh = null;
	private DynamicNamespaceContext dnc;

	private String elementJson = null;

	public PatternExtractor(Element element, Logger log) {
		this.element = element; 
		this.dnc = new DynamicNamespaceContext();
		this.dnc.findPrefixNamespace(element);
		this.log = log;
	}
	public PatternExtractor(String elementJson, Logger log) {
		this.elementJson = elementJson;
		this.log = log;
	}

	public void refreshContent() {
		if(this.element!=null && this.refresh==null) {
			this._refreshContent();
		}
	}
	private synchronized void _refreshContent() {
		// effettuo il refresh, altrimenti le regole xpath applicate sulla richiesta, nel flusso di risposta (es. header http della risposta) non funzionano.
		try {
			this.refresh = true;
			if(this.element!=null) {
				XMLUtils xmlUtils = XMLUtils.getInstance();
				this.element = xmlUtils.newElement(xmlUtils.toByteArray(this.element));
			}
		}catch(Exception e){
			this.log.error("Refresh fallito: "+e.getMessage(),e);
		}
	}

	public boolean match(String pattern) throws TrasformazioneException {
		String v = read(pattern);
		return v!=null && !"".equals(v);
	}

	public String read(String pattern) throws TrasformazioneException {
		String valore = null;
		try {
			if(this.element!=null) {
				AbstractXPathExpressionEngine xPathEngine = new org.openspcoop2.utils.xml.XPathExpressionEngine();
				valore = AbstractXPathExpressionEngine.extractAndConvertResultAsString(this.element, this.dnc, xPathEngine, pattern, this.log);
			}
			else {
				valore = JsonPathExpressionEngine.extractAndConvertResultAsString(this.elementJson, pattern, this.log);
			}
		}
		catch(XPathNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(XPathNotValidException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(JsonPathNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(JsonPathNotValidException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(org.openspcoop2.utils.UtilsMultiException e) {
			int index = 0;
			boolean notFound = true;
			boolean notValid = true;
			for (Throwable t : e.getExceptions()) {
				if(t instanceof XPathNotFoundException || t instanceof JsonPathNotFoundException) {
					this.log.debug("Estrazione ("+index+") '"+pattern+"' fallita: "+t.getMessage(),t);
				}
				else {
					notFound = false;
				}

				if(!(t instanceof XPathNotValidException) && !(t instanceof JsonPathNotValidException)) {
					notValid = false;
				}

				index++;
			}
			if(!notFound) {
				if(notValid) {
					throw new TrasformazioneException(e.getMessage(),e);
				}
				else {
					throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
				}
			}
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}

	public List<String> readList(String pattern) throws TrasformazioneException {
		List<String> valore = null;
		try {
			if(this.element!=null) {
				AbstractXPathExpressionEngine xPathEngine = new org.openspcoop2.utils.xml.XPathExpressionEngine();
				xPathEngine.getMatchPattern(this.element, this.dnc, pattern, XPathReturnType.BOOLEAN);
				valore = AbstractXPathExpressionEngine.extractAndConvertResultAsList(this.element, this.dnc, xPathEngine, pattern, this.log);
			}
			else {
				valore = JsonPathExpressionEngine.extractAndConvertResultAsList(this.elementJson, pattern, this.log);
			}
		}
		catch(XPathNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(XPathNotValidException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(JsonPathNotFoundException e){
			this.log.debug("Estrazione '"+pattern+"' non ha trovato risultati: "+e.getMessage(),e);
		}
		catch(JsonPathNotValidException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(org.openspcoop2.utils.UtilsMultiException e) {
			int index = 0;
			boolean notFound = true;
			boolean notValid = true;
			for (Throwable t : e.getExceptions()) {
				if(t instanceof XPathNotFoundException || t instanceof JsonPathNotFoundException) {
					this.log.debug("Estrazione ("+index+") '"+pattern+"' fallita: "+t.getMessage(),t);
				}
				else {
					notFound = false;
				}

				if(!(t instanceof XPathNotValidException) && !(t instanceof JsonPathNotValidException)) {
					notValid = false;
				}

				index++;
			}
			if(!notFound) {
				if(notValid) {
					throw new TrasformazioneException(e.getMessage(),e);
				}
				else {
					throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
				}
			}
		}
		catch(Exception e){
			throw new TrasformazioneException("Estrazione '"+pattern+"' fallita: "+e.getMessage(),e);
		}
		return valore;
	}
}
