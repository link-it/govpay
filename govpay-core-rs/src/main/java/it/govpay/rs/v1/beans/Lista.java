package it.govpay.rs.v1.beans;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter(value="lista") 
public abstract class Lista<T> extends JSONSerializable {

	private long numRisultati;
	private long numPagine;
	private long risultatiPerPagina;
	private long pagina;
	private String prossimiRisultati;
	
	private List<T> risultati;
	
	public List<T> getRisultati() {
		return risultati;
	}
	public void setRisultati(List<T> risultati) {
		this.risultati = risultati;
	}
	public long getNumRisultati() {
		return numRisultati;
	}
	public void setNumRisultati(long numRisultati) {
		this.numRisultati = numRisultati;
	}
	public long getNumPagine() {
		return numPagine;
	}
	public void setNumPagine(long numPagine) {
		this.numPagine = numPagine;
	}
	public long getRisultatiPerPagina() {
		return risultatiPerPagina;
	}
	public void setRisultatiPerPagina(long risultatiPerPagina) {
		this.risultatiPerPagina = risultatiPerPagina;
	}
	public long getPagina() {
		return pagina;
	}
	public void setPagina(long pagina) {
		this.pagina = pagina;
	}
	public String getProssimiRisultati() {
		return prossimiRisultati;
	}
	public void setProssimiRisultati(String prossimiRisultati) {
		this.prossimiRisultati = prossimiRisultati;
	}
	
	@Override
	public String getJsonIdFilter() {
		return "lista";
	}
	
	public Lista(List<T> risultati, URI requestUri, long count, long pagina, long risultatiPerPagina) {
		this.risultati = risultati;
		this.numPagine = (count < risultatiPerPagina) ? 1 : count / risultatiPerPagina;
		this.pagina = pagina;
		this.risultatiPerPagina = risultatiPerPagina;
		this.numRisultati = risultati.size();
		
		
		URIBuilder builder = new URIBuilder(requestUri);
		builder.setParameter("risultatiPerPagina", Long.toString(this.risultatiPerPagina));
		
		if(this.pagina < this.numPagine) {
			long nextPagina = this.pagina+1;
			builder.setParameter("pagina", Long.toString(nextPagina));
			try {
				this.prossimiRisultati = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
	}
	
}
