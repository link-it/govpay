/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.UnmarshalException;

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
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import gov.telematici.pagamenti.ws.CtEsitoAvvisatura;
import gov.telematici.pagamenti.ws.CtEsitoAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtEsitoPresaInCarico;
import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.EsitoAvvisatura;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.EsitiAvvisaturaBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.EsitoAvvisaturaFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.pagamento.util.CountPerDominio;
import it.govpay.core.beans.tracciati.Avvisatura;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.ConnettoreSftp;
import it.govpay.model.Intermediario;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;

public class Operazioni{

	private static Logger log = LoggerWrapperFactory.getLogger(Operazioni.class);
	public static final String RND = "update-rnd";
	public static final String PSP = "update-psp";
	public static final String PND = "update-pnd";
	public static final String NTFY = "update-ntfy";
	public static final String CHECK_NTFY = "check-ntfy";
	public static final String BATCH_TRACCIATI = "caricamento-tracciati";
	public static final String CHECK_TRACCIATI = "check-tracciati";
	public static final String BATCH_GENERAZIONE_AVVISI = "generazione-avvisi";
	public static final String BATCH_AVVISATURA_DIGITALE = "avvisatura-digitale";
	public static final String BATCH_ESITO_AVVISATURA_DIGITALE = "esito-avvisatura-digitale";



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

	private static boolean eseguiElaborazioneTracciati;
	
	public static synchronized void setEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = true;
	}
	
	public static synchronized void resetEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = false;
	}
	
	public static synchronized boolean getEseguiElaborazioneTracciati() {
		return eseguiElaborazioneTracciati;
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
			if(BatchManager.startEsecuzione(bd, RND)) {
				String response = new Rendicontazioni(bd).downloadRendicontazioni(false);
				aggiornaSondaOK(RND, bd);
				return response;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			aggiornaSondaKO(RND, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, RND);
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
			if(BatchManager.startEsecuzione(bd, PND)) {
				String verificaTransazioniPendenti = new Pagamento(bd).verificaTransazioniPendenti();
				aggiornaSondaOK(PND, bd);
				return verificaTransazioniPendenti;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			aggiornaSondaKO(PND, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, PND);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String spedizioneNotifiche(String serviceName){
		BasicBD bd = null;
		List<InviaNotificaThread> threads = new ArrayList<>();
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

			if(BatchManager.startEsecuzione(bd, NTFY)) {
				log.trace("Spedizione notifiche non consegnate");
				NotificheBD notificheBD = new NotificheBD(bd);
				List<Notifica> notifiche  = notificheBD.findNotificheDaSpedire();
				if(notifiche.size() == 0) {
					aggiornaSondaOK(NTFY, bd);
					BatchManager.stopEsecuzione(bd, NTFY);
					aggiornaSondaOK(NTFY, bd);
					log.info("Nessuna notifica da inviare.");
					return "Nessuna notifica da inviare.";
				}

				log.info("Trovate ["+notifiche.size()+"] notifiche da spedire");
				for(Notifica notifica: notifiche) {
					InviaNotificaThread sender = new InviaNotificaThread(notifica, bd);
					ThreadExecutorManager.getClientPoolExecutor().execute(sender);
					threads.add(sender);
				}
				log.info("Processi di spedizione avviati.");
				aggiornaSondaOK(NTFY, bd);
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			aggiornaSondaKO(NTFY, e, bd); 
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
					BatchManager.stopEsecuzione(bd, NTFY);
				} catch (ServiceException e) {
				} finally {
					if(bd != null) bd.closeConnection();
				}
				log.info("Spedizione notifiche completata.");
				return "Spedizione notifiche completata.";
			} else {
				try {
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
					BatchManager.aggiornaEsecuzione(bd, NTFY);
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

//	public static String generaAvvisi(String serviceName){
//		BasicBD bd = null;
//		GpContext ctx = null;
//		try {
//			ctx = new GpContext();
//			MDC.put("cmd", "GenerazioneAvvisi");
//			MDC.put("op", ctx.getTransactionId());
//			Service service = new Service();
//			service.setName(serviceName);
//			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
//			ctx.getTransaction().setService(service);
//			Operation opt = new Operation();
//			opt.setName("GenerazioneAvvisi");
//			ctx.getTransaction().setOperation(opt);
//			GpThreadLocal.set(ctx);
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			if(BatchManager.startEsecuzione(bd, batch_generazione_avvisi)) {
//				log.trace("Generazione Avvisi");
//				// 1. Recupero gli avvisi
//				AvvisoPagamento avvisoBD = new AvvisoPagamento(bd);
//				ListaAvvisiDTO listaAvvisi = new ListaAvvisiDTO();
//				listaAvvisi.setStato(StatoAvviso.DA_STAMPARE);
//				int offset = 0;
//				int limit = 500; 
//				listaAvvisi.setOffset(offset);
//				listaAvvisi.setLimit(limit);
//
//				ListaAvvisiDTOResponse listaAvvisiDTOResponse = avvisoBD.getAvvisi(listaAvvisi);
//
//				List<it.govpay.model.avvisi.AvvisoPagamento> avvisi = listaAvvisiDTOResponse.getAvvisi();
//				log.info("Trovati ["+avvisi.size()+"] avvisi da generare");
//
//				Versamento versamentoBD = new Versamento(bd);
//
//				while(avvisi.size() > 0) {
//
//					for (it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento : avvisi) {
//						String codDominio = avvisoPagamento.getCodDominio();
//						String iuv = avvisoPagamento.getIuv();
//						log.info("Generazione Avviso [Dominio: "+codDominio + " | IUV: "+ iuv+"] in corso...");
//						PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
//						printAvvisoDTO.setAvviso(avvisoPagamento);
//						it.govpay.bd.model.Versamento chiediVersamento = versamentoBD.chiediVersamento(null, null, null, null, codDominio, iuv);
//						AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, chiediVersamento);
//						printAvvisoDTO.setInput(input); 
//						PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
//						boolean pdfGenerato = printAvvisoDTOResponse.getAvviso().getPdf() != null;
//						log.info("Generazione Avviso [Dominio: "+codDominio + " | IUV: "+ iuv+"] "+(pdfGenerato ? "completata con successo" : "non completata")+".");
//					}
//
//					offset += avvisi.size();
//					listaAvvisi.setOffset(offset);
//					listaAvvisiDTOResponse = avvisoBD.getAvvisi(listaAvvisi);
//					avvisi = listaAvvisiDTOResponse.getAvvisi();
//				}
//
//				aggiornaSondaOK(batch_generazione_avvisi, bd);
//				BatchManager.stopEsecuzione(bd, batch_generazione_avvisi);
//				log.info("Generazione Avvisi Pagamento terminata.");
//				return "Generazione Avvisi Pagamento terminata.";
//			} else {
//				return "Operazione in corso su altro nodo. Richiesta interrotta.";
//			}
//		} catch (Exception e) {
//			log.error("Generazione Avvisi Pagamento Fallita", e);
//			aggiornaSondaKO(batch_generazione_avvisi, e, bd);
//			return "Generazione Avvisi Pagamento#" + e.getMessage();
//		} finally {
//			BatchManager.stopEsecuzione(bd, batch_generazione_avvisi);
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//		}
//	}

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
			log.warn("Errore nell'aggiornamento della sonda OK: "+ t.getMessage());
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
			log.warn("Errore nell'aggiornamento della sonda KO: "+ t.getMessage());
		} finally {
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	public static String esitoAvvisaturaDigitale(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {


			ctx = new GpContext();
			MDC.put("cmd", "EsitoAvvisaturaDigitale");
			MDC.put("op", ctx.getTransactionId());
			log.trace("Batch esito avvisatura digitale");
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("EsitoAvvisaturaDigitale");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			if(BatchManager.startEsecuzione(bd, BATCH_ESITO_AVVISATURA_DIGITALE)) {

				SerializationConfig config = new SerializationConfig();
				config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				config.setIgnoreNullValues(true);

				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
				ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

				List<Intermediario>	intermediari = new IntermediariBD(bd).getIntermediari();
				TracciatiBD tracciatiBD = new TracciatiBD(bd);

				for(Intermediario intermediario : intermediari) {

					if(intermediario.getConnettoreSftp()!=null) {
						// Accedo al server sftp e cerco nuovi flussi di esito avvisatura
						log.debug("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
						JSch jsch = new JSch();
						Session sessionIn = null;
						Channel channelIn = null;
						try {
							sessionIn = jsch.getSession(intermediario.getConnettoreSftp().getHttpUserIn(), intermediario.getConnettoreSftp().getUrlIn(), 22);
							sessionIn.setConfig("StrictHostKeyChecking", "no");
							sessionIn.setPassword(intermediario.getConnettoreSftp().getHttpPasswIn());
							sessionIn.connect();

							channelIn = sessionIn.openChannel("sftp");
							channelIn.connect();
							ChannelSftp sftpChannel = (ChannelSftp) channelIn;

							// Cerco file di richiesta Avvisatura che finiscono per _AV.zip
							log.debug("Cerco tracciati di Avvisatura digitale...");
							Vector<ChannelSftp.LsEntry> entryList = sftpChannel.ls(intermediario.getCodIntermediario() + "*_AV_ESITO.zip");
							log.debug("Trovati " + entryList.size() + " tracciati.");

							// Scorro i files e provo ad acquisirli
							for(ChannelSftp.LsEntry entry : entryList) {

								TracciatoFilter filter = tracciatiBD.newFilter();
								filter.setFilenameRichiesta(entry.getFilename());

								if(tracciatiBD.count(filter) == 0) {

									log.debug("Inserimento tracciato di esito avvisatura " + entry.getFilename() + "...");

									Tracciato tracciato = new Tracciato();

									String[] split = entry.getFilename().split("_");
									if(split.length != 6) {
										throw new Exception("Nome file "+entry.getFilename()+" non corrispondente a specifica");
									}


									tracciato.setCodDominio(split[1]);
									tracciato.setTipo(TIPO_TRACCIATO.AV_ESITO);
									tracciato.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
									tracciato.setDataCaricamento(new Date());
									tracciato.setFileNameRichiesta(entry.getFilename());
									tracciato.setFileNameEsito(entry.getFilename().replace("_AV_ESITO.zip", "_AV_ESITO_ACK.zip"));

									it.govpay.core.beans.tracciati.EsitoAvvisatura esitoAvvisatura = new it.govpay.core.beans.tracciati.EsitoAvvisatura();

									esitoAvvisatura.setNumeroEsiti(0);
									esitoAvvisatura.setStepElaborazione("DA_ACQUISIRE");
									esitoAvvisatura.setDataUltimoAggiornamento(new Date());
									esitoAvvisatura.setPercentualeStep(0);

									tracciato.setBeanDati(serializer.getObject(esitoAvvisatura));

									tracciatiBD.insertTracciato(tracciato);

									log.debug("Inserimento tracciato di esito avvisatura " + entry.getFilename() + " completato con successo");
								} else {
									log.debug("Esito avvisatura " + entry.getFilename() + " gia' acquisito con successo");
								}
							}
							sftpChannel.exit();
						} catch (JSchException | SftpException e) {
							log.error("Non e' stato possibile connettersi al server sftp", e);
						} finally {
							if(channelIn != null) channelIn.disconnect();
							if(sessionIn != null) sessionIn.disconnect();
						}

					} else {
						log.trace("Nessun connettore SFTP configurato per l'intermediario " + intermediario.getCodIntermediario());
					}
				}



				log.trace("Lettura tracciati in elaborazione...");
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.setTipo(Arrays.asList(TIPO_TRACCIATO.AV_ESITO));
				filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
				List<Tracciato> tracciati = tracciatiBD.findAll(filter);

				if(!tracciati.isEmpty()) {
					log.debug("Trovati "+tracciati.size()+" tracciati in elaborazione...");
					EsitiAvvisaturaBD esitiAvvisaturaBD = new EsitiAvvisaturaBD(bd);

//					XMLInputFactory xif = XMLInputFactory.newInstance();
//					TransformerFactory tf = TransformerFactory.newInstance(com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl.class.getName(), null );
//					Transformer t = tf.newTransformer();

					for(Tracciato tracciato: tracciati) {


						it.govpay.core.beans.tracciati.EsitoAvvisatura beanDati = (it.govpay.core.beans.tracciati.EsitoAvvisatura) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.EsitoAvvisatura.class);

						Dominio dominio = AnagraficaManager.getDominio(bd, tracciato.getCodDominio());
						Intermediario intermediario = dominio.getStazione().getIntermediario(bd);

						if("DA_ACQUISIRE".equals(beanDati.getStepElaborazione())) {
							log.debug("Tracciato "+tracciato.getId()+" DA_ACQUISIRE...");


							EsitoAvvisaturaFilter filterByIdTracciato = esitiAvvisaturaBD.newFilter();
							filterByIdTracciato.setIdTracciato(tracciato.getId());
							esitiAvvisaturaBD.deleteAll(filterByIdTracciato);

							ZipInputStream zis = null;
							Session sessionIn = null;
							Channel channelIn = null;
							try {
								// Accedo al server sftp e cerco nuovi flussi di rendicontazione
								log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
								JSch jsch = new JSch();
								ConnettoreSftp connettore = intermediario.getConnettoreSftp();

								if(connettore == null)
									throw new Exception("Connettore SFTP in ingresso non definito per l'intermediario "+intermediario.getCodIntermediario());

								sessionIn = jsch.getSession(connettore.getHttpUserIn(), connettore.getUrlIn(), 22);
								sessionIn.setConfig("StrictHostKeyChecking", "no");
								sessionIn.setPassword(connettore.getHttpPasswIn());
								sessionIn.connect();

								channelIn = sessionIn.openChannel("sftp");
								channelIn.connect();
								ChannelSftp sftpChannel = (ChannelSftp) channelIn;
								InputStream inputStream = sftpChannel.get(tracciato.getFileNameRichiesta());
								if(inputStream != null) {
									zis = new ZipInputStream(inputStream);

									zis.getNextEntry();

//									XMLStreamReader xsr = xif.createXMLStreamReader(zis);
//									CtEsitoAvvisoDigitale esitoAvvisoDigitale = AvvisaturaUtils.leggiProssimoEsitoAvvisoDigitale(xsr, t);

									List<it.govpay.model.EsitoAvvisatura> esitoAvvisaturaLst = new ArrayList<>();


									List<CtEsitoAvvisoDigitale> leggiListaAvvisiDigitali = AvvisaturaUtils.leggiListaAvvisiDigitali(zis);
									for(CtEsitoAvvisoDigitale esitoAvvisoDigitale: leggiListaAvvisiDigitali) {

										for(CtEsitoAvvisatura ctEsitoAvvisatura: esitoAvvisoDigitale.getEsitoAvvisatura()) {
											EsitoAvvisatura esitoAvvisatura = new EsitoAvvisatura();
											esitoAvvisatura.setCodDominio(esitoAvvisoDigitale.getIdentificativoDominio());
											esitoAvvisatura.setIdentificativoAvvisatura(esitoAvvisoDigitale.getIdentificativoMessaggioRichiesta());

											esitoAvvisatura.setTipoCanale(Integer.parseInt(ctEsitoAvvisatura.getTipoCanaleEsito()));
											esitoAvvisatura.setCodCanale(ctEsitoAvvisatura.getIdentificativoCanale());
											esitoAvvisatura.setData(ctEsitoAvvisatura.getDataEsito().toGregorianCalendar().getTime());
											esitoAvvisatura.setCodEsito(ctEsitoAvvisatura.getCodiceEsito());
											esitoAvvisatura.setDescrizioneEsito(ctEsitoAvvisatura.getDescrizioneEsito());
											esitoAvvisatura.setIdTracciato(tracciato.getId());

											esitoAvvisaturaLst.add(esitoAvvisatura);

//											if(esitoAvvisaturaLst.size() > 100) { //TODO batch e definire dimensione nel properties
//												esitiAvvisaturaBD.insertEsitoAvvisaturaBatch(esitoAvvisaturaLst);
//												esitoAvvisaturaLst = new ArrayList<>();
//											}
										}

									}
									
									//inserisco i record residui
									esitiAvvisaturaBD.insertEsitoAvvisaturaBatch(esitoAvvisaturaLst);

									beanDati.setDataUltimoAggiornamento(new Date());
									beanDati.setStepElaborazione("ACQUISITO");
									beanDati.setCodiceEsitoPresaInCarico(0);

									tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));


								} else { //non ho trovato il file di esito, non succedera' mai. Aggiorno la data ultimo aggiornamento del bean dati
									beanDati.setDataUltimoAggiornamento(new Date());
									tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
								}
							} catch(UnmarshalException e) {
								log.error("Errore durante la lettura del file di esito'"+tracciato.getFileNameEsito()+"':"+ e.getMessage(),e);
								beanDati.setDescrizioneStepElaborazione(e.getMessage());
								beanDati.setCodiceEsitoPresaInCarico(1);
								beanDati.setDescrizioneEsitoPresaInCarico("File in ingresso non ben formato");
								beanDati.setDataUltimoAggiornamento(new Date());
								beanDati.setStepElaborazione("ACQUISITO");
								tracciato.setBeanDati(serializer.getObject(beanDati));
								tracciatiBD.update(tracciato);
							} catch(Exception e) {
								log.error("Errore durante la lettura del file di esito'"+tracciato.getFileNameEsito()+"':"+ e.getMessage(),e);
								beanDati.setDescrizioneStepElaborazione(e.getMessage());
								beanDati.setDataUltimoAggiornamento(new Date());
								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
							} finally {
								try {if(zis !=null) zis.close();} catch(IOException e) {}
								if(channelIn != null) channelIn.disconnect();
								if(sessionIn != null) sessionIn.disconnect();
							}

						} else if("ACQUISITO".equals(beanDati.getStepElaborazione())) {
							log.debug("Tracciato "+tracciato.getId()+" ACQUISITO...");
							Session sessionOut = null;
							Channel channelOut = null;
							ZipOutputStream zipOutputStreamFileRichiesta = null;
							try {
								// Accedo al server sftp e cerco nuovi flussi di rendicontazione
								log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
								JSch jsch = new JSch();
								ConnettoreSftp connettore = intermediario.getConnettoreSftp();
								if(connettore == null)
									throw new Exception("Connettore SFTP in uscita non definito per l'intermediario "+intermediario.getCodIntermediario());

								sessionOut = jsch.getSession(connettore.getHttpUserOut(), connettore.getUrlOut(), 22);
								sessionOut.setConfig("StrictHostKeyChecking", "no");
								sessionOut.setPassword(connettore.getHttpPasswOut());
								sessionOut.connect();

								channelOut = sessionOut.openChannel("sftp");
								channelOut.connect();
								ChannelSftp sftpChannel = (ChannelSftp) channelOut;


								// Cerco file che finiscono per _AV.zip
								log.info("Scrittura tracciato "+tracciato.getFileNameEsito()+" di Avvisatura digitale...");

								OutputStream outputStreamFileRichiesta = sftpChannel.put(tracciato.getFileNameEsito());
								zipOutputStreamFileRichiesta = new ZipOutputStream(outputStreamFileRichiesta);
								zipOutputStreamFileRichiesta.putNextEntry(new ZipEntry(tracciato.getFileNameEsito().replaceAll("zip", "xml")));

								CtEsitoPresaInCarico esito = new CtEsitoPresaInCarico();
								esito.setCodiceEsitoPresaInCarico(BigInteger.valueOf(beanDati.getCodiceEsitoPresaInCarico()));
								esito.setDescrizioneEsitoPresaInCarico(beanDati.getDescrizioneEsitoPresaInCarico());
								esito.setIdentificativoFlusso(tracciato.getFileNameEsito().replace(".zip", ""));
								
								AvvisaturaUtils.scriviEsitoPresaInCarico(esito, zipOutputStreamFileRichiesta);
								


								zipOutputStreamFileRichiesta.flush();
								zipOutputStreamFileRichiesta.close();
								
								sftpChannel.exit();
								sessionOut.disconnect();


								log.info("Scrittura tracciato "+tracciato.getFileNameEsito()+" di Avvisatura digitale completata");

								beanDati.setStepElaborazione("ACKATO");
								beanDati.setDataUltimoAggiornamento(new Date());
								beanDati.setPercentualeStep(0);

								tracciato.setStato(beanDati.getCodiceEsitoPresaInCarico() != 0 ? STATO_ELABORAZIONE.SCARTATO: STATO_ELABORAZIONE.COMPLETATO);
								tracciato.setDataCompletamento(new Date());
								tracciato.setBeanDati(serializer.getObject(beanDati));
								tracciatiBD.update(tracciato);
							} catch(Exception e) {
								log.error("Errore durante la scrittura del file di ack'"+tracciato.getFileNameEsito()+"':"+ e.getMessage(),e);
								beanDati.setDescrizioneStepElaborazione(e.getMessage());
								beanDati.setDataUltimoAggiornamento(new Date());
								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
							} finally {
								try {if(zipOutputStreamFileRichiesta !=null) zipOutputStreamFileRichiesta.close();} catch(IOException e) {}
								if(channelOut!= null) channelOut.disconnect();
								if(sessionOut!= null) sessionOut.disconnect();
							}

							
						}

					}

				} else {
					log.debug("Nessun tracciato in elaborazione trovato");
				}

				log.debug("Batch esito avvisatura digitale completato");


				aggiornaSondaOK(BATCH_ESITO_AVVISATURA_DIGITALE, bd);

				return "Esito Avvisatura Digitale OK";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Esito Avvisatura Digitale Fallita", e);
			try {
				if(bd.isAutoCommit())
					bd.rollback();
				aggiornaSondaKO(BATCH_ESITO_AVVISATURA_DIGITALE, e, bd);
			} catch (ServiceException e1) {
				log.error("Aggiornamento sonda fallito: " + e.getMessage(),e);
			}
			return "Esito Avvisatura Digitale#" + e.getMessage();
		} finally {
			BatchManager.stopEsecuzione(bd, BATCH_ESITO_AVVISATURA_DIGITALE);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
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

			if(BatchManager.startEsecuzione(bd, BATCH_AVVISATURA_DIGITALE)) {
				log.debug("Batch avvisatura digitale");



				SerializationConfig config = new SerializationConfig();
				config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				config.setIgnoreNullValues(true);

				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
				ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);



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
						Session sessionOut = null;
						Channel channelOut = null;
						ZipOutputStream zipOutputStreamFileRichiesta = null;
						try {
							// Accedo al server sftp e cerco nuovi flussi di rendicontazione
							log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
							JSch jsch = new JSch();
							ConnettoreSftp connettore = intermediario.getConnettoreSftp();
							if(connettore == null)
								throw new Exception("Connettore SFTP in uscita non definito per l'intermediario "+intermediario.getCodIntermediario());

							sessionOut = jsch.getSession(connettore.getHttpUserOut(), connettore.getUrlOut(), 22);
							sessionOut.setConfig("StrictHostKeyChecking", "no");
							sessionOut.setPassword(connettore.getHttpPasswOut());
							sessionOut.connect();

							channelOut = sessionOut.openChannel("sftp");
							channelOut.connect();
							ChannelSftp sftpChannel = (ChannelSftp) channelOut;


							// Cerco file che finiscono per _AV.zip
							log.info("Scrittura tracciato "+tracciato.getFileNameRichiesta()+" di Avvisatura digitale...");

							OutputStream outputStreamFileRichiesta = sftpChannel.put(tracciato.getFileNameRichiesta());
							zipOutputStreamFileRichiesta = new ZipOutputStream(outputStreamFileRichiesta);

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
								avvisatura.setPercentualeStep(Math.round(((long) versamentiFilter.getOffset() / count) * 100.0f));

								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
								versamentiFilter.setOffset(versamentiFilter.getOffset() + lstVersamenti.size());
								lstVersamenti = versamentiBD.findAll(versamentiFilter);

							}

							AvvisaturaUtils.scriviTailTracciatoAvvisatura(zipOutputStreamFileRichiesta);

							zipOutputStreamFileRichiesta.flush();
							zipOutputStreamFileRichiesta.close();
							
							sftpChannel.exit();
							sessionOut.disconnect();


							log.info("Scrittura tracciato "+tracciato.getFileNameRichiesta()+" di Avvisatura digitale completata");

							avvisatura.setStepElaborazione("SPEDITO");
							avvisatura.setDataUltimoAggiornamento(new Date());
							avvisatura.setPercentualeStep(0);

							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
						} catch(Exception e) {
							log.error("Errore durante la scrittura del file '"+tracciato.getFileNameRichiesta()+"':"+ e.getMessage(),e);
							avvisatura.setStepElaborazione("ERRORE_SPEDIZIONE");
							avvisatura.setDataUltimoAggiornamento(new Date());
							avvisatura.setDescrizioneStepElaborazione(e.getMessage());
							avvisatura.setPercentualeStep(0);

							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
						} finally {
							if(channelOut != null) channelOut.disconnect();
							if(sessionOut != null) sessionOut.disconnect();
							if(zipOutputStreamFileRichiesta != null) zipOutputStreamFileRichiesta.close();
						}
					} else if("SPEDITO".equals(avvisatura.getStepElaborazione())) {

						ZipInputStream zis = null;
						Session sessionIn = null;
						Channel channelIn = null;

						try {
							// Accedo al server sftp e cerco nuovi flussi di rendicontazione
							log.info("Accedo al servizio SFTP per l'intermediario " + intermediario.getCodIntermediario());
							JSch jsch = new JSch();
							ConnettoreSftp connettore = intermediario.getConnettoreSftp();

							if(connettore == null)
								throw new Exception("Connettore SFTP in ingresso non definito per l'intermediario "+intermediario.getCodIntermediario());

							sessionIn = jsch.getSession(connettore.getHttpUserIn(), connettore.getUrlIn(), 22);
							sessionIn.setConfig("StrictHostKeyChecking", "no");
							sessionIn.setPassword(connettore.getHttpPasswIn());
							sessionIn.connect();

							channelIn = sessionIn.openChannel("sftp");
							channelIn.connect();
							ChannelSftp sftpChannel = (ChannelSftp) channelIn;
							InputStream inputStream = sftpChannel.get(tracciato.getFileNameEsito());
							if(inputStream != null) {
								zis = new ZipInputStream(inputStream);

								zis.getNextEntry();
								
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
							} else { //non ho trovato il file di ack
								avvisatura.setDataUltimoAggiornamento(new Date());
								tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
							}
						} catch(Exception e) {
							log.warn("Errore durante la lettura del file di ack'"+tracciato.getFileNameEsito()+"':"+ e.getMessage(),e);
							avvisatura.setDescrizioneStepElaborazione(e.getMessage());
							avvisatura.setDataUltimoAggiornamento(new Date());
							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(avvisatura));
						} finally {
							try {if(zis !=null) zis.close();} catch(IOException e) {}
							if(channelIn != null) channelIn.disconnect();
							if(sessionIn != null) sessionIn.disconnect();
						}

					}

				}


				SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd"); 
				VersamentoFilter versamentiFilter = versamentiBD.newFilter();
				versamentiFilter.setDaAvvisare(true);
				versamentiFilter.setTracciatoNull(true);
				List<CountPerDominio> countGroupByIdDominio = versamentiBD.countGroupByIdDominio(versamentiFilter);

				boolean wasAutoCommit = bd.isAutoCommit();
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

				log.debug("Batch avvisatura digitale terminato");

				aggiornaSondaOK(BATCH_AVVISATURA_DIGITALE, bd);

				if(!wasAutoCommit)
					bd.setAutoCommit(wasAutoCommit);

				return "Avvisatura digitale ok";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}

		} catch (Exception e) {
			log.error("Avvisatura digitale Fallita", e);
			try {
				if(bd.isAutoCommit())
					bd.rollback();
				aggiornaSondaKO(BATCH_AVVISATURA_DIGITALE, e, bd);
			} catch (ServiceException e1) {
				log.error("Aggiornamento sonda fallito: " + e.getMessage(),e);
			}
			return "Avvisatura digitale#" + e.getMessage();
		} finally {
			BatchManager.stopEsecuzione(bd, BATCH_AVVISATURA_DIGITALE);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String elaborazioneTracciati(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			MDC.put("cmd", "ElaborazioneTracciati");
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("CaricamentoTracciato");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			if(BatchManager.startEsecuzione(bd, BATCH_TRACCIATI)) {
				bd.setAutoCommit(false);
				log.trace("Elaborazione tracciati");
				
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.setTipo(Arrays.asList(TIPO_TRACCIATO.PENDENZA));
				filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
//				filter.setDataUltimoAggiornamentoMax(new Date());
				List<Tracciato> tracciati = tracciatiBD.findAll(filter);
				Tracciati tracciatiBusiness = new Tracciati(bd);
				
				while(!tracciati.isEmpty()) {
					log.info("Trovati ["+tracciati.size()+"] tracciati da elaborare...");
					
					for(Tracciato tracciato: tracciati) {
						log.info("Avvio elaborazione tracciato "  + tracciato.getId());
						ElaboraTracciatoDTO elaboraTracciatoDTO = new ElaboraTracciatoDTO();
						elaboraTracciatoDTO.setTracciato(tracciato);
						tracciatiBusiness.elaboraTracciato(elaboraTracciatoDTO);
						log.info("Elaborazione tracciato "  + tracciato.getId() + " completata");
					}
					
//					filter.setDataUltimoAggiornamentoMax(new Date());
					tracciati = tracciatiBD.findAll(filter);
				}
				
				aggiornaSondaOK(BATCH_TRACCIATI, bd);
				BatchManager.stopEsecuzione(bd, BATCH_TRACCIATI);
				log.info("Elaborazione tracciati terminata.");
				return "Elaborazione tracciati terminata.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			if(bd != null) {
				bd.rollback();
				aggiornaSondaKO(BATCH_TRACCIATI, e, bd); 
			}
			log.error("Non è stato possibile eseguire l'elaborazione dei tracciati", e);
			return "Non è stato possibile eseguire l'elaborazione dei tracciati: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}
