package it.govpay.core.utils.thread;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.CtAvvisoDigitale;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GpContext;

public class InviaAvvisaturaThread implements Runnable {

	private static Logger log = LoggerWrapperFactory.getLogger(InviaAvvisaturaThread.class);
	private boolean completed = false;
	private Versamento versamento = null;
	private CtAvvisoDigitale avviso = null;

	public InviaAvvisaturaThread(Versamento versamento, BasicBD bd) throws ServiceException {
		this.versamento = versamento;
		//leggo le info annidate
		this.versamento.getDominio(bd);

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
			ctx = new GpContext();
			
			
		} catch(Exception e) {
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
