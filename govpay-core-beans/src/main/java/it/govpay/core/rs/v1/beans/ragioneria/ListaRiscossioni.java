package it.govpay.core.rs.v1.beans.ragioneria;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.rs.v1.beans.Lista;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaRiscossioni extends Lista<RiscossioneIndex> {
	
	public ListaRiscossioni(List<RiscossioneIndex> riscossioni, URI requestUri, long count, long pagina, long limit) {
		super(riscossioni, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
	
}
