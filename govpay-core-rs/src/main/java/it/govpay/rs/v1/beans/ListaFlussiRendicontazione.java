package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.rs.v1.beans.base.FlussoRendicontazioneIndex;

public class ListaFlussiRendicontazione extends Lista<FlussoRendicontazioneIndex> {
	
	public ListaFlussiRendicontazione() {
		super();
	}
	
	public ListaFlussiRendicontazione(List<FlussoRendicontazioneIndex> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
