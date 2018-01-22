package it.govpay.bd.pagamento;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.model.PagamentoPortale;

public class PagamentiPortaleBD extends BasicBD{
	
	
	private static Map<Long, PagamentoPortale> dbPagamentiPortale;
	private static Long nextId = 0L;
	
	static {
		dbPagamentiPortale = new HashMap<Long, PagamentoPortale>();
	}
	
	public static Long getNextId() {
		return  ++ nextId ;
	}

	public PagamentiPortaleBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Crea un nuovo pagamento.
	 */
	public void insertPagamento(PagamentoPortale pagamentoPortale) throws ServiceException {
		// salvataggio della entry sul db [TODO] bussu
		Long id = PagamentiPortaleBD.getNextId();
		PagamentiPortaleBD.dbPagamentiPortale.put(id, pagamentoPortale);
		pagamentoPortale.setId(id); 
	}

	public void updatePagamento(PagamentoPortale pagamento) throws ServiceException {
		PagamentiPortaleBD.dbPagamentiPortale.remove(pagamento.getId());
		PagamentiPortaleBD.dbPagamentiPortale.put(pagamento.getId(), pagamento);
	}
	
	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public PagamentoPortale getPagamento(long id) throws ServiceException {
		return PagamentiPortaleBD.dbPagamentiPortale.get(id);
	}
	
	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public PagamentoPortale getPagamento(String codSessione) throws ServiceException {
		for (Long key : PagamentiPortaleBD.dbPagamentiPortale.keySet()) {
			PagamentoPortale pagamentoPortale = PagamentiPortaleBD.dbPagamentiPortale.get(key);
			if(pagamentoPortale.getIdSessione().equals(codSessione))
				return pagamentoPortale;
		}
		
		return null;
	}
}
