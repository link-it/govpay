package it.govpay.rs.legacy.beans;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.costanti.Costanti;

@JsonFilter(value="lista") 
public class Lista<T extends JSONSerializable> extends JSONSerializable {

	private long numRisultati;
	private long numPagine;
	private long risultatiPerPagina;
	private long pagina;
	private String prossimiRisultati;
	
	private List<T> risultati;
	
	public List<T> getRisultati() {
		return this.risultati;
	}
	public void setRisultati(List<T> risultati) {
		this.risultati = risultati;
	}
	public long getNumRisultati() {
		return this.numRisultati;
	}
	public void setNumRisultati(long numRisultati) {
		this.numRisultati = numRisultati;
	}
	public long getNumPagine() {
		return this.numPagine;
	}
	public void setNumPagine(long numPagine) {
		this.numPagine = numPagine;
	}
	public long getRisultatiPerPagina() {
		return this.risultatiPerPagina;
	}
	public void setRisultatiPerPagina(long risultatiPerPagina) {
		this.risultatiPerPagina = risultatiPerPagina;
	}
	public long getPagina() {
		return this.pagina;
	}
	public void setPagina(long pagina) {
		this.pagina = pagina;
	}
	public String getProssimiRisultati() {
		return this.prossimiRisultati;
	}
	public void setProssimiRisultati(String prossimiRisultati) {
		this.prossimiRisultati = prossimiRisultati;
	}
	
	@Override
	public String getJsonIdFilter() {
		return "lista";
	}
	
	public Lista() {
		
	}
	
	public String toJSONArray(String fields) throws ServiceException {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		for (T t : this.risultati) {
			if(sb.length() > 1)
				sb.append(",");
			
			sb.append(t.toJSON(fields));
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	public Lista(List<T> risultati, URI requestUri, long count, long pagina, long limit) {

		this.risultati = risultati;
		this.numPagine = count == 0 ? 1 : (long) Math.ceil(count/(double)limit);
//		this.pagina = (long) Math.ceil((offset+1)/(double)limit);
		this.pagina = pagina;
		this.risultatiPerPagina = limit;
		this.numRisultati = count;
		
		
		URIBuilder builder = new URIBuilder(requestUri);
		builder.setParameter(Costanti.PARAMETRO_RISULTATI_PER_PAGINA, Long.toString(this.risultatiPerPagina));
		
		if(this.pagina < this.numPagine) {
//			long nextPagina = offset+limit;
			long nextPagina = pagina + 1 ;
			builder.setParameter(Costanti.PARAMETRO_PAGINA, Long.toString(nextPagina));
			try {
				this.prossimiRisultati = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
	}
	
}
