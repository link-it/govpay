package it.govpay.core.business;

import java.util.Calendar;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.pagamento.StampeBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.model.Stampa.TIPO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;

public class Svecchiamento {

	private static Logger log = LoggerWrapperFactory.getLogger(Svecchiamento.class);

	public Svecchiamento() {
	}

	public String eseguiSvecchiamento(IContext ctx) throws ServiceException {
		it.govpay.bd.model.Configurazione configurazione = null;
		it.govpay.bd.configurazione.model.Svecchiamento svecchiamento = null;
		try {
			configurazione = new it.govpay.core.business.Configurazione().getConfigurazione();

			svecchiamento = configurazione.getSvecchiamento();
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la procedura di svecchiamento", e);
			return "Non è stato possibile avviare la procedura di svecchiamento: " + e.getMessage();
		}

		StringBuffer sb = new StringBuffer();

		try {
			// Avvisi Pagamento e Ricevute Pagamento
			this.eseguiSvecchiamentoAvvisiPagamento(ctx, svecchiamento, sb);

			this.eseguiSvecchiamentoRicevutePagamento(ctx, svecchiamento, sb);

			// Tracciati
			this.eseguiSvecchiamentoTracciatiScartati(ctx, svecchiamento, sb);

			this.eseguiSvecchiamentoTracciatiCompletati(ctx, svecchiamento, sb);
			
			
			// Pendenze
			
			
			// Pagamenti
			
			
			// Rendicontazioni
			
			
			// Eventi
			
			
			// Notifiche
			
			
		} catch (ServiceException e) {
			log.error("Procedura di svecchiamento conclusa con errore: " + e.getMessage(), e);
			throw e;
		}

		return sb.toString();
	}

	private void eseguiSvecchiamentoAvvisiPagamento(IContext ctx, it.govpay.bd.configurazione.model.Svecchiamento svecchiamento, StringBuffer sb) {
		if(svecchiamento.getStampeAvvisi() == null) {
			sb.append("Svecchiamento delle stampe di avviso pagamento disabilitato.");
			return;
		}

		int giorniSvecchiamento = svecchiamento.getStampeAvvisi().intValue();
		log.debug("Eliminazione delle stampe di avviso pagamento piu' vecchie di ["+giorniSvecchiamento+"] giorni...");

		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		StampeBD stampeBD = new StampeBD(configWrapper);

		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		instance.add(Calendar.DATE, - giorniSvecchiamento);

		Date dataCreazione = instance.getTime();
		try {
			long numeroStampeElininate = stampeBD.deleteStampePiuVecchieDiData(TIPO.AVVISO, dataCreazione);
			log.debug("Sono state eliminate ["+numeroStampeElininate+"] stampe di avviso pagamento.");
			sb.append("Sono state eliminate ["+numeroStampeElininate+"] stampe di avviso pagamento.");
		} catch (ServiceException e) {
			log.error("Si e' verificato un errore durante l'esecuzione dello svecchiamento delle stampe di avviso pagamento: ", e);
		}
	}

	private void eseguiSvecchiamentoRicevutePagamento(IContext ctx, it.govpay.bd.configurazione.model.Svecchiamento svecchiamento, StringBuffer sb) {
		if(svecchiamento.getStampeRicevute() == null) {
			sb.append("Svecchiamento delle stampe di ricevuta pagamento disabilitato.");
			return;
		}

		int giorniSvecchiamento = svecchiamento.getStampeRicevute().intValue();
		log.debug("Eliminazione delle stampe di ricevuta pagamento piu' vecchie di ["+giorniSvecchiamento+"] giorni...");

		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		StampeBD stampeBD = new StampeBD(configWrapper);

		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		instance.add(Calendar.DATE, - giorniSvecchiamento);

		Date dataCreazione = instance.getTime();
		try {
			long numeroStampeElininate = stampeBD.deleteStampePiuVecchieDiData(TIPO.RICEVUTA, dataCreazione); 
			log.debug("Sono state eliminate ["+numeroStampeElininate+"] stampe di ricevuta pagamento.");
			sb.append("Sono state eliminate ["+numeroStampeElininate+"] stampe di ricevuta pagamento.");
		} catch (ServiceException e) {
			log.error("Si e' verificato un errore durante l'esecuzione dello svecchiamento delle stampe di ricevuta pagamento: ", e);
		}
	}

	private void eseguiSvecchiamentoTracciatiScartati(IContext ctx, it.govpay.bd.configurazione.model.Svecchiamento svecchiamento, StringBuffer sb) throws ServiceException{
		if(svecchiamento.getTracciatiPendenzeScartati() == null) {
			sb.append("Svecchiamento dei tracciati scartati disabilitato.");
			return;
		}

		int giorniSvecchiamento = svecchiamento.getTracciatiPendenzeScartati().intValue();
		log.debug("Eliminazione dei tracciati scartati piu' vecchi di ["+giorniSvecchiamento+"] giorni...");

		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiBD tracciatiBD = null;
		try {

			tracciatiBD = new TracciatiBD(configWrapper);

			tracciatiBD.setupConnection(configWrapper.getTransactionID());

			tracciatiBD.setAtomica(false);

			tracciatiBD.setAutoCommit(false); 

			Date dataCompletamento = calcolaDataSogliaEliminazione(giorniSvecchiamento);
			try {
				long numeroTracciatiEliminati = tracciatiBD.deleteTracciatiPiuVecchiDiData(TIPO_TRACCIATO.PENDENZA, STATO_ELABORAZIONE.SCARTATO, dataCompletamento, true, true);
				log.debug("Sono stati eliminati ["+numeroTracciatiEliminati+"] tracciati scartati.");
				sb.append("Sono stati eliminati ["+numeroTracciatiEliminati+"] tracciati scartati.");
			} catch (ServiceException e) {
				log.error("Si e' verificato un errore durante l'esecuzione dello svecchiamento dei tracciati scartati: ", e);
				tracciatiBD.rollback();
			}
		} finally {
			if(tracciatiBD != null) {
				tracciatiBD.closeConnection();
			}
		}
	}

	private void eseguiSvecchiamentoTracciatiCompletati(IContext ctx, it.govpay.bd.configurazione.model.Svecchiamento svecchiamento, StringBuffer sb) throws ServiceException{
		if(svecchiamento.getTracciatiPendenzeCompletati() == null) {
			sb.append("Svecchiamento dei tracciati completati disabilitato.");
			return;
		}

		int giorniSvecchiamento = svecchiamento.getTracciatiPendenzeCompletati().intValue();
		log.debug("Eliminazione dei tracciati completati piu' vecchi di ["+giorniSvecchiamento+"] giorni...");

		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiBD tracciatiBD = null;
		try {

			tracciatiBD = new TracciatiBD(configWrapper);

			tracciatiBD.setupConnection(configWrapper.getTransactionID());

			tracciatiBD.setAtomica(false);

			tracciatiBD.setAutoCommit(false); 

			Date dataCompletamento = calcolaDataSogliaEliminazione(giorniSvecchiamento);
			try {
				long numeroTracciatiEliminati = tracciatiBD.deleteTracciatiPiuVecchiDiData(TIPO_TRACCIATO.PENDENZA, STATO_ELABORAZIONE.COMPLETATO, dataCompletamento, true, true);
				log.debug("Sono stati eliminati ["+numeroTracciatiEliminati+"] tracciati completati.");
				sb.append("Sono stati eliminati ["+numeroTracciatiEliminati+"] tracciati completati.");
			} catch (ServiceException e) {
				log.error("Si e' verificato un errore durante l'esecuzione dello svecchiamento dei tracciati completati: ", e);
				tracciatiBD.rollback();
			}
		} finally {
			if(tracciatiBD != null) {
				tracciatiBD.closeConnection();
			}
		}
	}

	private Date calcolaDataSogliaEliminazione(int giorniSvecchiamento) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		instance.add(Calendar.DATE, - giorniSvecchiamento);

		Date dataCreazione = instance.getTime();
		return dataCreazione;
	}
}
