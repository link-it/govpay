/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaBatch;
import org.slf4j.Logger;
import org.slf4j.MDC;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import gov.telematici.pagamenti.ws.CtEsitoPresaInCarico;
import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.pagamento.util.CountPerDominio;
import it.govpay.core.beans.tracciati.Avvisatura;
import it.govpay.core.business.model.ListaAvvisiDTO;
import it.govpay.core.business.model.ListaAvvisiDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.ConnettoreSftp;
import it.govpay.model.Intermediario;
import it.govpay.model.Tracciato;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.model.avvisi.AvvisoPagamentoInput;

public class Operazioni{

	private static Logger log = LoggerWrapperFactory.getLogger(Operazioni.class);
	public static final String rnd = "update-rnd";
	public static final String psp = "update-psp";
	public static final String pnd = "update-pnd";
	public static final String ntfy = "update-ntfy";
	public static final String check_ntfy = "check-ntfy";
	public static final String batch_generazione_avvisi = "generazione-avvisi";
	public static final String batch_avvisatura_digitale = "avvisatura-digitale";
	public static final String batch_esito_avvisatura_digitale = "esito-avvisatura-digitale";


	private static boolean eseguiGenerazioneAvvisi;

	public static synchronized void setEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = true;
	}

	public static synchronized void resetEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = false;
	}

	public static synchronized boolean getEseguiGenerazioneAvvisi() {
		return eseguiGenerazioneAvvisi;
	}

	public static String acquisizioneRendicontazioni(String serviceName){

		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "AcquisizioneRendicontazioni");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AcquisizioneRendicontazioni");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, rnd)) {
				String response = new Rendicontazioni(bd).downloadRendicontazioni(false);
				aggiornaSondaOK(rnd, bd);
				return response;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			aggiornaSondaKO(rnd, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, rnd);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String recuperoRptPendenti(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "RecuperoRptPendenti");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("RecuperoRptPendenti");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, pnd)) {
				String verificaTransazioniPendenti = new Pagamento(bd).verificaTransazioniPendenti();
				aggiornaSondaOK(pnd, bd);
				return verificaTransazioniPendenti;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			aggiornaSondaKO(pnd, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, pnd);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String spedizioneNotifiche(String serviceName){
		BasicBD bd = null;
		List<InviaNotificaThread> threads = new ArrayList<InviaNotificaThread>();
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "SpedizioneNotifiche");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("SpedizioneNotifiche");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			if(BatchManager.startEsecuzione(bd, ntfy)) {
				log.trace("Spedizione notifiche non consegnate");
				NotificheBD notificheBD = new NotificheBD(bd);
				List<Notifica> notifiche  = notificheBD.findNotificheDaSpedire();
				if(notifiche.size() == 0) {
					aggiornaSondaOK(ntfy, bd);
					BatchManager.stopEsecuzione(bd, ntfy);
					return "Nessuna notifica da inviare.";
				}

				log.info("Trovate ["+notifiche.size()+"] notifiche da spedire");
				for(Notifica notifica: notifiche) {
					InviaNotificaThread sender = new InviaNotificaThread(notifica, bd);
					ThreadExecutorManager.getClientPoolExecutor().execute(sender);
				}
				log.info("Processi di spedizione avviati.");
				aggiornaSondaOK(ntfy, bd);
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			aggiornaSondaKO(ntfy, e, bd); 
			return "Non è stato possibile avviare la spedizione delle notifiche: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}

		// Aspetto che abbiano finito tutti
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
			boolean completed = true;
			for(InviaNotificaThread sender : threads) {
				if(!sender.isCompleted()) 
					completed = false;
			}

			if(completed) {
				try {
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
					BatchManager.stopEsecuzione(bd, ntfy);
				} catch (ServiceException e) {
				} finally {
					if(bd != null) bd.closeConnection();
				}

				return "Spedizione notifiche completata.";
			} else {
				try {
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
					BatchManager.aggiornaEsecuzione(bd, ntfy);
				} catch (ServiceException e) {
				} finally {
					if(bd != null) bd.closeConnection();
				}
			}
		}
	}

	public static String resetCacheAnagrafica(){
		MDC.put("cmd", "ResetCacheAnagrafica");
		MDC.put("op", UUID.randomUUID().toString() );
		try {
			AnagraficaManager.cleanCache();
			return "Reset cache completata con successo.";
		} catch (Exception e) {
			log.error("Reset cache anagrafica fallita", e);
			return "Reset cache completata fallita: " + e;
		} 
	}

	public static String generaAvvisi(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "GenerazioneAvvisi");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("GenerazioneAvvisi");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, batch_generazione_avvisi)) {
				log.trace("Generazione Avvisi");
				// 1. Recupero gli avvisi
				AvvisoPagamento avvisoBD = new AvvisoPagamento(bd);
				ListaAvvisiDTO listaAvvisi = new ListaAvvisiDTO();
				listaAvvisi.setStato(StatoAvviso.DA_STAMPARE);
				int offset = 0;
				int limit = 500; 
				listaAvvisi.setOffset(offset);
				listaAvvisi.setLimit(limit);

				ListaAvvisiDTOResponse listaAvvisiDTOResponse = avvisoBD.getAvvisi(listaAvvisi);

				List<it.govpay.model.avvisi.AvvisoPagamento> avvisi = listaAvvisiDTOResponse.getAvvisi();
				log.info("Trovati ["+avvisi.size()+"] avvisi da generare");

				Versamento versamentoBD = new Versamento(bd);

				while(avvisi.size() > 0) {

					for (it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento : avvisi) {
						String codDominio = avvisoPagamento.getCodDominio();
						String iuv = avvisoPagamento.getIuv();
						log.info("Generazione Avviso [Dominio: "+codDominio + " | IUV: "+ iuv+"] in corso...");
						PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
						printAvvisoDTO.setAvviso(avvisoPagamento);
						it.govpay.bd.model.Versamento chiediVersamento = versamentoBD.chiediVersamento(null, null, null, null, codDominio, iuv);
						AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, chiediVersamento);
						printAvvisoDTO.setInput(input); 
						PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
						boolean pdfGenerato = printAvvisoDTOResponse.getAvviso().getPdf() != null;
						log.info("Generazione Avviso [Dominio: "+codDominio + " | IUV: "+ iuv+"] "+(pdfGenerato ? "completata con successo" : "non completata")+".");
					}

					offset += avvisi.size();
					listaAvvisi.setOffset(offset);
					listaAvvisiDTOResponse = avvisoBD.getAvvisi(listaAvvisi);
					avvisi = listaAvvisiDTOResponse.getAvvisi();
				}

				aggiornaSondaOK(batch_generazione_avvisi, bd);
				BatchManager.stopEsecuzione(bd, batch_generazione_avvisi);
				log.info("Generazione Avvisi Pagamento terminata.");
				return "Generazione Avvisi Pagamento terminata.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Generazione Avvisi Pagamento Fallita", e);
			aggiornaSondaKO(batch_generazione_avvisi, e, bd);
			return "Generazione Avvisi Pagamento#" + e.getMessage();
		} finally {
			BatchManager.stopEsecuzione(bd, batch_generazione_avvisi);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	private static void aggiornaSondaOK(String nome, BasicBD bd) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			}
			Connection con = bd.getConnection();

			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			//			Properties properties = new Properties();
			//			((SondaBatch)sonda).aggiornaStatoSonda(true, properties, new Date(), "Ok", con, bd.getJdbcProperties().getDatabase());
			((SondaBatch)sonda).aggiornaStatoSonda(true,  new Date(), "Ok", con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda OK", t);
		}
		finally {
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	private static void aggiornaSondaKO(String nome, Exception e, BasicBD bd) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			}
			Connection con = bd.getConnection();
			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			//			Properties properties = new Properties();
			//			((SondaBatch)sonda).aggiornaStatoSonda(false, properties, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
			((SondaBatch)sonda).aggiornaStatoSonda(false, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda KO", t);
		} finally {
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	public static String esitoAvvisaturaDigitale(String serviceName){
//		BasicBD bd = null;
//		GpContext ctx = null;
//		try {
//
//			log.info("Batch esito avvisatura digitale");
//
//			ctx = new GpContext();
//			MDC.put("cmd", "EsitoAvvisaturaDigitale");
//			MDC.put("op", ctx.getTransactionId());
//			Service service = new Service();
//			service.setName(serviceName);
//			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
//			ctx.getTransaction().setService(service);
//			Operation opt = new Operation();
//			opt.setName("EsitoAvvisaturaDigitale");
//			ctx.getTransaction().setOperation(opt);
//			GpThreadLocal.set(ctx);
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//
//
//			SerializationConfig config = new SerializationConfig();
//			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
//			config.setIgnoreNullValues(true);
//
//			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
//			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
//
//
//
//			boolean wasAutoCommit = bd.isAutoCommit();
//			if(wasAutoCommit)
//				bd.setAutoCommit(false);
//
//			VersamentiBD versamentiBD = new VersamentiBD(bd);
//			TracciatiBD tracciatiBD = new TracciatiBD(bd);
//			TracciatoFilter filter = tracciatiBD.newFilter();
//			filter.setTipo(TIPO_TRACCIATO.AV);
//			filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
//			List<Tracciato> tracciati = tracciatiBD.findAll(filter);	
//			return null;
//		} catch (Exception e) {
//			log.error("Esito Avvisatura Digitale Fallita", e);
//			aggiornaSondaKO(batch_esito_avvisatura_digitale, e, bd);
//			return "Esito Avvisatura Digitale#" + e.getMessage();
//		} finally {
//			BatchManager.stopEsecuzione(bd, batch_esito_avvisatura_digitale);
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//		}

		return null;
	}

	public static String avvisaturaDigitale(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "AvvisaturaDigitale");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AvvisaturaDigitale");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			if(BatchManager.startEsecuzione(bd, batch_avvisatura_digitale)) {
				log.info("Batch avvisatura digitale");



				SerializationConfig config = new SerializationConfig();
				config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				config.setIgnoreNullValues(true);

				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
				ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);



				boolean wasAutoCommit = bd.isAutoCommit();
				if(wasAutoCommit)
					bd.setAutoCommit(false);

				VersamentiBD versamentiBD = new VersamentiBD(bd);
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.setTipo(Arrays.asList(TIPO_TRACCIATO.AV));
				filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
				List<Tracciato> tracciati = tracciatiBD.findAll(filter);

				for(Tracciato tracciato: tracciati) {


					Avvisatura avvisatura = (Avvisatura) deserializer.getObject(tracciato.getBeanDati(), Avvisatura.class);

					Intermediario intermediario = AnagraficaManager.getIntermediario(bd, avvisatura.getIntermediario());

					if("IN_SPEDIZIONE".equals(avvisatura.getStepElaborazione()) || "ERRORE_SPEDIZIONE".equals(avvisatura.getStepElaborazione())) {
						try {
							// Accedo al server sftp e cerco nuovi flussi di rendicontazione
							log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
							JSch jsch = new JSch();
							Session session = null;
							ConnettoreSftp connettore = intermediario.getConnettoreSftp();
							if(connettore == null)
								throw new Exception("Connettore SFTP in uscita non definito per l'intermediario "+intermediario.getCodIntermediario());

							session = jsch.getSession(connettore.getHttpUserOut(), connettore.getUrlOut(), 22);
							session.setConfig("StrictHostKeyChecking", "no");
							session.setPassword(connettore.getHttpPasswOut());
							session.connect();

							Channel channel = session.openChannel("sftp");
							channel.connect();
							ChannelSftp sftpChannel = (ChannelSftp) channel;


							// Cerco file che finiscono per _AV.zip
							log.info("Scrittura tracciato "+tracciato.getFileNameRichiesta()+" di Avvisatura digitale...");

							OutputStream outputStreamFileRichiesta = sftpChannel.put(tracciato.getFileNameRichiesta());
							ZipOutputStream zipOutputStreamFileRichiesta = new ZipOutputStream(outputStreamFileRichiesta);

							VersamentoFilter versamentiFilter = versamentiBD.newFilter();
							versamentiFilter.setIdTracciato(tracciato.getId());
							versamentiFilter.setOffset(0);
							versamentiFilter.setLimit(GovpayConfig.getInstance().getSizePaginaNumeroVersamentiAvvisaturaDigitale());


							long count = versamentiBD.count(versamentiFilter);
							List<it.govpay.bd.model.Versamento> lstVersamenti = versamentiBD.findAll(versamentiFilter);

							zipOutputStreamFileRichiesta.putNextEntry(new ZipEntry(tracciato.getFileNameRichiesta().replaceAll("zip", "xml")));
							AvvisaturaUtils.scriviHeaderTracciatoAvvisatura(zipOutputStreamFileRichiesta);
							while(lstVersamenti!=null && !lstVersamenti.isEmpty()) {

								for(it.govpay.bd.model.Versamento versamento: lstVersamenti) {

									//leggo le info annidate
									versamento.getDominio(bd);

									List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
									for(SingoloVersamento singoloVersamento: singoliVersamenti) {
										singoloVersamento.getIbanAccredito(bd);
										singoloVersamento.getIbanAppoggio(bd);
									}
									AvvisaturaUtils.scriviVersamentoTracciatoAvvisatura(zipOutputStreamFileRichiesta, versamento);
								}


								avvisatura.setStepElaborazione("IN_SPEDIZIONE");
								avvisatura.setDataUltimoAggiornamento(new Date());
								avvisatura.setPercentualeStep(Math.round((versamentiFilter.getOffset() / count) * 100.0f));

								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
								bd.commit();

								versamentiFilter.setOffset(versamentiFilter.getOffset() + lstVersamenti.size());
								lstVersamenti = versamentiBD.findAll(versamentiFilter);

							}

							AvvisaturaUtils.scriviTailTracciatoAvvisatura(zipOutputStreamFileRichiesta);


							zipOutputStreamFileRichiesta.flush();
							zipOutputStreamFileRichiesta.close();

							sftpChannel.exit();
							session.disconnect();


							log.info("Scrittura tracciato "+tracciato.getFileNameRichiesta()+" di Avvisatura digitale completata");

							avvisatura.setStepElaborazione("SPEDITO");
							avvisatura.setDataUltimoAggiornamento(new Date());
							avvisatura.setPercentualeStep(0);

							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
							bd.commit();
						} catch(Exception e) {
							log.error("Errore durante la scrittura del file '"+tracciato.getFileNameRichiesta()+"':"+ e.getMessage(),e);
							avvisatura.setStepElaborazione("ERRORE_SPEDIZIONE");
							avvisatura.setDataUltimoAggiornamento(new Date());
							avvisatura.setDescrizioneStepElaborazione(e.getMessage());
							avvisatura.setPercentualeStep(0);

							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
							bd.commit();
						}
					} else if("SPEDITO".equals(avvisatura.getStepElaborazione())) {

						ZipInputStream zis = null;
						try {
							// Accedo al server sftp e cerco nuovi flussi di rendicontazione
							log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
							JSch jsch = new JSch();
							Session session = null;
							ConnettoreSftp connettore = intermediario.getConnettoreSftp();

							if(connettore == null)
								throw new Exception("Connettore SFTP in ingresso non definito per l'intermediario "+intermediario.getCodIntermediario());

							session = jsch.getSession(connettore.getHttpUserIn(), connettore.getUrlIn(), 22);
							session.setConfig("StrictHostKeyChecking", "no");
							session.setPassword(connettore.getHttpPasswIn());
							session.connect();

							Channel channel = session.openChannel("sftp");
							channel.connect();
							ChannelSftp sftpChannel = (ChannelSftp) channel;
							InputStream inputStream = sftpChannel.get(tracciato.getFileNameEsito());
							if(inputStream != null) {
								zis = new ZipInputStream(inputStream);

								ZipEntry zipEntry = null;
								try {
									zipEntry = zis.getNextEntry();
								} catch (IOException e) {
									log.warn("Archivio " + tracciato.getFileNameEsito() + " vuoto");
									throw e;
								}

								CtEsitoPresaInCarico esitoPresaInCaricoAvvisoDigitale = AvvisaturaUtils.leggiEsitoPresaInCaricoAvvisoDigitale(zis);

								avvisatura.setStepElaborazione("ACKATO");
								avvisatura.setDescrizioneStepElaborazione(null);
								avvisatura.setPercentualeStep(0);
								avvisatura.setCodiceEsitoPresaInCarico(esitoPresaInCaricoAvvisoDigitale.getCodiceEsitoPresaInCarico().intValue());
								avvisatura.setDescrizioneEsitoPresaInCarico(esitoPresaInCaricoAvvisoDigitale.getDescrizioneEsitoPresaInCarico());
								avvisatura.setDataUltimoAggiornamento(new Date());

								tracciato.setStato(avvisatura.getCodiceEsitoPresaInCarico() != 0 ? STATO_ELABORAZIONE.SCARTATO: STATO_ELABORAZIONE.COMPLETATO);
								tracciato.setDataCompletamento(new Date());
								tracciato.setBeanDati(serializer.getObject(avvisatura));

								tracciatiBD.update(tracciato);
								bd.commit();
							} else { //non ho trovato il file di ack
								avvisatura.setDataUltimoAggiornamento(new Date());
								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
								bd.commit();
							}
						} catch(Exception e) {
							log.error("Errore durante la lettura del file di ack'"+tracciato.getFileNameEsito()+"':"+ e.getMessage(),e);
							avvisatura.setDescrizioneStepElaborazione(e.getMessage());
							avvisatura.setDataUltimoAggiornamento(new Date());
							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
							bd.commit();
						} finally {
							try {if(zis !=null) zis.close();} catch(IOException e) {}
						}

					}

				}

				if(!wasAutoCommit)
					bd.setAutoCommit(wasAutoCommit);



				SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("YYYYMMdd"); 
				VersamentoFilter versamentiFilter = versamentiBD.newFilter();
				versamentiFilter.setDaAvvisare(true);
				versamentiFilter.setTracciatoNull(true);
				List<CountPerDominio> countGroupByIdDominio = versamentiBD.countGroupByIdDominio(versamentiFilter);

				wasAutoCommit = bd.isAutoCommit();
				if(wasAutoCommit)
					bd.setAutoCommit(false);
				for(CountPerDominio countPerDominio: countGroupByIdDominio) {

					Dominio dominio = AnagraficaManager.getDominio(bd, countPerDominio.getIdDominio());
					Intermediario intermediario = dominio.getStazione().getIntermediario(bd);

					long countVersamentiTracciati = 0;
					String filenameFormat = intermediario.getCodIntermediario() + "_" + dominio.getCodDominio() + "_" + sdfYYYYMMDD.format(new Date());
					TracciatoFilter tracciatiFilter = tracciatiBD.newFilter();
					tracciatiFilter.setFilenameRichiestaLike(filenameFormat);
					tracciatiFilter.setTipo(Arrays.asList(TIPO_TRACCIATO.AV));
					long countTracciatiPerDominioPergiorno = tracciatiBD.count(tracciatiFilter);

					while(countVersamentiTracciati < countPerDominio.getCount()) {
						long countVersamentiNextTracciato = Math.min(countPerDominio.getCount()-countVersamentiTracciati, GovpayConfig.getInstance().getSizePaginaNumeroVersamentiAvvisaturaDigitale());

						Tracciato tracciato = new Tracciato();
						//populate tracciato
						tracciato.setCodDominio(dominio.getCodDominio());
						tracciato.setTipo(TIPO_TRACCIATO.AV);
						tracciato.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
						tracciato.setDataCaricamento(new Date());
						Avvisatura avvisatura = new Avvisatura();
						avvisatura.setIntermediario(intermediario.getCodIntermediario());
						avvisatura.setNumeroAvvisi(countVersamentiNextTracciato);
						avvisatura.setStepElaborazione("IN_SPEDIZIONE");
						avvisatura.setDataUltimoAggiornamento(new Date());
						avvisatura.setPercentualeStep(0);
						tracciato.setBeanDati(serializer.getObject(avvisatura));
						tracciato.setFileNameRichiesta(filenameFormat + "_" + String.format("%02d", countTracciatiPerDominioPergiorno) + "_" + TIPO_TRACCIATO.AV.toString() + ".zip");
						tracciato.setFileNameEsito(filenameFormat + "_" + String.format("%02d", countTracciatiPerDominioPergiorno) + "_" + TIPO_TRACCIATO.AV.toString() + "_ACK.zip");

						tracciatiBD.insertTracciato(tracciato);
						versamentiBD.updateConLimit(countPerDominio.getIdDominio(), tracciato.getId(), countVersamentiNextTracciato);
						bd.commit();

						countVersamentiTracciati += countVersamentiNextTracciato;
						countTracciatiPerDominioPergiorno++;
					}

				}

				log.info("Batch avvisatura digitale terminato");

				aggiornaSondaOK(batch_avvisatura_digitale, bd);

				if(!wasAutoCommit)
					bd.setAutoCommit(wasAutoCommit);

				return "Avvisatura digitale ok";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}

		} catch (Exception e) {
			log.error("Avvisatura digitale Fallita", e);
			aggiornaSondaKO(batch_avvisatura_digitale, e, bd);
			return "Avvisatura digitale#" + e.getMessage();
		} finally {
			BatchManager.stopEsecuzione(bd, batch_avvisatura_digitale);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}