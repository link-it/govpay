package it.govpay.core.rs.v1.beans.ragioneria;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaRiscossioni extends Lista<RiscossioneIndex> {
	
	public ListaRiscossioni(List<RiscossioneIndex> riscossioni, URI requestUri, long count, long pagina, long limit) {
		super(riscossioni, requestUri, count, pagina, limit);
	}
	
}
