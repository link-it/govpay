package it.govpay.core.business.model;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.govpay.model.Dominio;

public class EstrattoConto {
	
	public enum TipoEstrattoConto {
		CSV , PDF 
	}

	private Dominio dominio;
	private List<Long> idVersamenti;
	private Map<String, ByteArrayOutputStream> output;
	private TipoEstrattoConto tipoEstrattoConto;
	
	public EstrattoConto(){
		
	}
 
	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public List<Long> getIdVersamenti() {
		return idVersamenti;
	}
	public void setIdVersamenti(List<Long> idVersamenti) {
		this.idVersamenti = idVersamenti;
	}
	public TipoEstrattoConto getTipoEstrattoConto() {
		return tipoEstrattoConto;
	}
	public void setTipoEstrattoConto(TipoEstrattoConto tipoEstrattoConto) {
		this.tipoEstrattoConto = tipoEstrattoConto;
	}
	public Map<String, ByteArrayOutputStream> getOutput() {
		if(output == null)
			output = new HashMap<String, ByteArrayOutputStream>();
		return output;
	}

	public static EstrattoConto creaEstrattoContoPDF(){
		EstrattoConto ec = new EstrattoConto();
		ec.setTipoEstrattoConto(TipoEstrattoConto.PDF);
		return ec;
	}
	
	public static EstrattoConto creaEstrattoContoPDF(Dominio dominio,List<Long> idVersamenti){
		EstrattoConto ec = new EstrattoConto();
		ec.setDominio(dominio);
		ec.setIdVersamenti(idVersamenti);
		ec.setTipoEstrattoConto(TipoEstrattoConto.PDF);
		return ec;
	}
	
}
