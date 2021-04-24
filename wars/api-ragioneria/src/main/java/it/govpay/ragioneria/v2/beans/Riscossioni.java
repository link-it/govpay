package it.govpay.ragioneria.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class Riscossioni extends Lista<RiscossioneIndex> {
	
	public Riscossioni(List<RiscossioneIndex> riscossioni, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(riscossioni, requestUri, count, pagina, limit);
	}
	
}
