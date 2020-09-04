package it.govpay.core.utils.thread;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.orm.IdTracciato;

public class CreaStampeTracciatoThread implements Runnable {
	
	private List<Versamento> versamenti = null;
	private List<PrintAvvisoDTOResponse> stampe = null;
	
	private boolean completed = false;
	private static Logger log = LoggerWrapperFactory.getLogger(CreaStampeTracciatoThread.class);
	@SuppressWarnings("unused")
	private IdTracciato idTracciato = null;
	private IContext ctx = null;
	private String nomeThread = "";
	
	public CreaStampeTracciatoThread(List<Versamento> versamenti, IdTracciato idTracciato, String id, IContext ctx) {
		this.versamenti = versamenti;
		this.idTracciato = idTracciato;
		this.ctx = ctx;
		this.nomeThread = id;
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		this.stampe = new ArrayList<PrintAvvisoDTOResponse>();
		BasicBD bd = null;
		try {
			bd = setupConnection(bd);
			log.debug("Creazione stampe di " + this.versamenti.size() + " versamenti...");
			it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
			
			for (Versamento versamento : versamenti) {
					
				PrintAvvisoDTOResponse printAvvisoDTOResponse =  null;
				
				if(versamento.getNumeroAvviso() != null) {
					if(versamento.getDocumento(bd) != null) {
						
						PrintAvvisoDocumentoDTO printDocumentoDTO = new PrintAvvisoDocumentoDTO();
						printDocumentoDTO.setDocumento(versamento.getDocumento(bd));
						printDocumentoDTO.setUpdate(true);
						printAvvisoDTOResponse = avvisoBD.printAvvisoDocumento(printDocumentoDTO);
						printAvvisoDTOResponse.setCodDocumento(versamento.getDocumento(bd).getCodDocumento());
					} else {
						PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
						printAvvisoDTO.setUpdate(true);
						printAvvisoDTO.setCodDominio(versamento.getDominio(bd).getCodDominio());
						printAvvisoDTO.setIuv(versamento.getIuvVersamento());
						printAvvisoDTO.setVersamento(versamento); 
						printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
					}
					
					printAvvisoDTOResponse.setCodDominio(versamento.getDominio(bd).getCodDominio()); 
					printAvvisoDTOResponse.setNumeroAvviso(versamento.getNumeroAvviso());
					this.stampe.add(printAvvisoDTOResponse);
				} else {
					log.debug("Pendenza [IDA2A: " + versamento.getApplicazione(bd).getCodApplicazione()	
							+" | IdPendenza: " + versamento.getCodVersamentoEnte() + "] non ha numero avviso, procedura di stampa non eseguita.");
				}
			}
			
		}catch(ServiceException e) {
			log.error("Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
			
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			log.debug("Stampe prodotte: " + this.stampe.size());
			ContextThreadLocal.unset();
		}
		
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	private BasicBD setupConnection(BasicBD bd) throws ServiceException {
		if(bd == null) {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
		} else {
			if(bd.isClosed())
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
		}
		
		return bd;
	}

	public List<PrintAvvisoDTOResponse> getStampe() {
		return stampe;
	}

	public String getNomeThread() {
		return nomeThread;
	}
	
	
}
