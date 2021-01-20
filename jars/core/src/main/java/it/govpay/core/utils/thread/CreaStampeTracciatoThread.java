package it.govpay.core.utils.thread;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.orm.IdTracciato;

public class CreaStampeTracciatoThread implements Runnable {
	
	private List<Versamento> versamenti = null;
	private List<PrintAvvisoDTOResponse> stampe = null;
	
	private boolean completed = false;
	private boolean commit = false;
	private static Logger log = LoggerWrapperFactory.getLogger(CreaStampeTracciatoThread.class);
	@SuppressWarnings("unused")
	private IdTracciato idTracciato = null;
	private IContext ctx = null;
	private String nomeThread = "";
	private int stampeOk = 0;
	private int stampeKo = 0;
		
	public CreaStampeTracciatoThread(List<Versamento> versamenti, IdTracciato idTracciato, String id, IContext ctx) {
		this.versamenti = versamenti;
		this.idTracciato = idTracciato;
		this.ctx = ctx;
		this.nomeThread = id;
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		this.stampe = new ArrayList<PrintAvvisoDTOResponse>();
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		try {
			log.debug("Creazione stampe di " + this.versamenti.size() + " versamenti...");
			it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
			
			for (Versamento versamento : versamenti) {
					
				PrintAvvisoDTOResponse printAvvisoDTOResponse =  null;
				try {
					if(versamento.getNumeroAvviso() != null) {
						Documento documento = versamento.getDocumento(configWrapper);
						if(documento != null) {
							
							PrintAvvisoDocumentoDTO printDocumentoDTO = new PrintAvvisoDocumentoDTO();
							printDocumentoDTO.setDocumento(documento);
							printDocumentoDTO.setUpdate(true);
							printDocumentoDTO.setSalvaSuDB(false);
							printAvvisoDTOResponse = avvisoBD.printAvvisoDocumento(printDocumentoDTO);
							printAvvisoDTOResponse.setCodDocumento(documento.getCodDocumento());
						} else {
							PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
							printAvvisoDTO.setUpdate(true);
							printAvvisoDTO.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
							printAvvisoDTO.setIuv(versamento.getIuvVersamento());
							printAvvisoDTO.setVersamento(versamento);
							printAvvisoDTO.setSalvaSuDB(false);
							printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
						}
						
						printAvvisoDTOResponse.setCodDominio(versamento.getDominio(configWrapper).getCodDominio()); 
						printAvvisoDTOResponse.setNumeroAvviso(versamento.getNumeroAvviso());
						this.stampe.add(printAvvisoDTOResponse);
					} else {
						log.debug("Pendenza [IDA2A: " + versamento.getApplicazione(configWrapper).getCodApplicazione()	
								+" | IdPendenza: " + versamento.getCodVersamentoEnte() + "] non ha numero avviso, procedura di stampa non eseguita.");
					}
					stampeOk ++;
				}catch(ServiceException e) {
					log.error("Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
					stampeKo ++;
				}
			}
		} finally {
			this.completed = true;
			log.debug("Stampe prodotte: " + this.stampe.size());
			ContextThreadLocal.unset();
		}
		
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public List<PrintAvvisoDTOResponse> getStampe() {
		return stampe;
	}

	public String getNomeThread() {
		return nomeThread;
	}

	public int getStampeOk() {
		return stampeOk;
	}

	public int getStampeKo() {
		return stampeKo;
	}

	public boolean isCommit() {
		return commit;
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}
}
