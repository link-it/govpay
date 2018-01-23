package it.govpay.bd.pagamento;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;

public class PagamentiPortaleBD extends BasicBD{
	
	
	private static Map<Long, PagamentoPortale> dbPagamentiPortale;
	private static Long nextId = 0L;
	
	public static Long getNextId() {
		return  ++ nextId ;
	}
	
	public static Map<Long, PagamentoPortale> getDB() {
		if(dbPagamentiPortale == null)
			init();
		
		return dbPagamentiPortale;
	}
	
	public static synchronized void init() {
		if(dbPagamentiPortale == null) {
			dbPagamentiPortale = new HashMap<Long, PagamentoPortale>();
			nextId = 0L;
		}
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
		PagamentiPortaleBD.getDB().put(id, pagamentoPortale);
		pagamentoPortale.setId(id); 
	}

	public void updatePagamento(PagamentoPortale pagamento) throws ServiceException {
		PagamentiPortaleBD.getDB().remove(pagamento.getId());
		PagamentiPortaleBD.getDB().put(pagamento.getId(), pagamento);
	}
	
	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public PagamentoPortale getPagamento(long id) throws ServiceException {
		return PagamentiPortaleBD.getDB().get(id);
	}
	
	/**
	 * Recupera il pagamento identificato dal codSessione
	 */
	public PagamentoPortale getPagamentoFromCodSessione(String codSessione) throws ServiceException,NotFoundException {
		for (Long key : PagamentiPortaleBD.getDB().keySet()) {
			PagamentoPortale pagamentoPortale = PagamentiPortaleBD.getDB().get(key);
			if(pagamentoPortale.getIdSessione().equals(codSessione))
				return pagamentoPortale;
		}
		
		throw new NotFoundException("Pagamento con idSessione "+codSessione+" non trovato.");
	}
	
	/**
	 * Recupera il pagamento identificato dal codsessionepsp
	 */
	public PagamentoPortale getPagamentoFromCodSessionePsp(String codSessione) throws ServiceException,NotFoundException {
		for (Long key : PagamentiPortaleBD.getDB().keySet()) {
			PagamentoPortale pagamentoPortale = PagamentiPortaleBD.getDB().get(key);
			if(pagamentoPortale.getIdSessionePsp().equals(codSessione))
				return pagamentoPortale;
		}
		
		throw new NotFoundException("Pagamento con idSessionePsp "+codSessione+" non trovato.");
	}
}
