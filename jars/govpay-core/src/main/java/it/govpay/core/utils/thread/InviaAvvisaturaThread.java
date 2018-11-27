package it.govpay.core.utils.thread;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;
import org.slf4j.MDC;

import gov.telematici.pagamenti.ws.CtAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtNodoInviaAvvisoDigitaleRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.AvvisaturaClient;
import it.govpay.core.utils.client.AvvisaturaClient.Azione;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Intermediario;

public class InviaAvvisaturaThread implements Runnable {

	private static Logger log = LoggerWrapperFactory.getLogger(InviaAvvisaturaThread.class);
	private boolean completed = false;
	private Versamento versamento = null;
	private Intermediario intermediario = null;
	private Stazione stazione = null;
	private CtAvvisoDigitale avviso = null;
	private String idTransazione = null;

	public InviaAvvisaturaThread(Versamento versamento, String idTransazione, BasicBD bd) throws ServiceException {
		this.idTransazione = idTransazione;
		this.versamento = versamento;
		//leggo le info annidate
		this.versamento.getDominio(bd);
		this.stazione = this.versamento.getDominio(bd).getStazione();
		this.intermediario = this.stazione.getIntermediario(bd);

		List<SingoloVersamento> singoliVersamenti = this.versamento.getSingoliVersamenti(bd);
		for(SingoloVersamento singoloVersamento: singoliVersamenti) {
			singoloVersamento.getIbanAccredito(bd);
			singoloVersamento.getIbanAppoggio(bd);
		}
		
		try {
			this.avviso = AvvisaturaUtils.toCtAvvisoDigitale(versamento);
		} catch (UnsupportedEncodingException | DatatypeConfigurationException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void run() {
		
		GpContext ctx = null;
		BasicBD bd = null;
		try {
			if(this.idTransazione != null)
				ctx = new GpContext(this.idTransazione);
			else 
				ctx = new GpContext();
			
			GpThreadLocal.set(ctx);
			MDC.put("cmd", "InviaAvvisaturaThread");
			MDC.put("op", ctx.getTransactionId());
			
			ctx.setupNodoClient(this.stazione.getCodStazione(), this.avviso.getIdentificativoDominio(), Azione.nodoInviaAvvisoDigitale);
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", this.avviso.getIdentificativoDominio()));
			ctx.getContext().getRequest().addGenericProperty(new Property("codAvviso", this.avviso.getCodiceAvviso()));
			ctx.getContext().getRequest().addGenericProperty(new Property("tipoOperazione", this.avviso.getTipoOperazione().name()));
			
			log.info("Spedizione Avvisatura al Nodo [Dominio: " + this.avviso.getIdentificativoDominio() + ", NumeroAvviso: "+this.avviso.getCodiceAvviso()+", TipoAvvisatura: "+this.avviso.getTipoOperazione()+"]");
			
			AvvisaturaClient client = new AvvisaturaClient(this.intermediario,bd);
			
			
			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale = new CtNodoInviaAvvisoDigitale();
			ctNodoInviaAvvisoDigitale.setAvvisoDigitaleWS(this.avviso);
			ctNodoInviaAvvisoDigitale.setPassword(this.stazione.getPassword()); 
			
			CtNodoInviaAvvisoDigitaleRisposta risposta = client.nodoInviaAvvisoDigitale(this.intermediario, this.stazione, ctNodoInviaAvvisoDigitale );
			
			
			log.info("RPT inviata correttamente al nodo");
			ctx.log("pagamento.invioRptAttivataOk");
		} catch (ClientException e) {
			log.error("Errore nella spedizione della RPT", e);
			ctx.log("pagamento.invioRptAttivataFail", e.getMessage());
		} catch(Exception e) {
			log.error("Errore nella spedizione della RPT", e);
			ctx.log("pagamento.invioRptAttivataFail", e.getMessage());
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			if(ctx != null) ctx.log();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
}
