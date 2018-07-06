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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaBatch;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.core.business.model.ListaAvvisiDTO;
import it.govpay.core.business.model.ListaAvvisiDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.model.avvisi.AvvisoPagamentoInput;

public class Operazioni{

	private static Logger log = LoggerWrapperFactory.getLogger(Operazioni.class);
	public static final String rnd = "update-rnd";
	public static final String pnd = "update-pnd";
	public static final String ntfy = "update-ntfy";
	public static final String check_ntfy = "check-ntfy";
	public static final String batch_generazione_avvisi = "generazione-avvisi";
	public static final String batch_avvisatura_digitale = "avvisatura-digitale";


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

//    public static String avvisaturaDigitale(String serviceName){
//		BasicBD bd = null;
//		GpContext ctx = null;
//		try {
//			ctx = new GpContext();
//			MDC.put("cmd", "AvvisaturaDigitale");
//			MDC.put("op", ctx.getTransactionId());
//			Service service = new Service();
//			service.setName(serviceName);
//			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
//			ctx.getTransaction().setService(service);
//			Operation opt = new Operation();
//			opt.setName("AvvisaturaDigitale");
//			ctx.getTransaction().setOperation(opt);
//			GpThreadLocal.set(ctx);
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//
//	        // Scandisco tutte le stazioni censite
//	        List<Stazione> stazioni;
//	        try {
//	            stazioni = new StazioniBD().getStazioni();
//	            for(Stazione stazione : stazioni) {
//	
//	                // Accedo al server sftp e cerco nuovi flussi di rendicontazione
//	                log.info("Accedo al servizio SFTP per la stazione " + stazione.getId_stazione());
//	                JSch jsch = new JSch();
//	                Session session = null;
//	                try {
//	                    session = jsch.getSession(stazione.getSftp_in_user(), stazione.getSftp_in_host(), 22);
//	                    session.setConfig("StrictHostKeyChecking", "no");
//	                    session.setPassword(stazione.getSftp_in_pass());
//	                    session.connect();
//	
//	                    Channel channel = session.openChannel("sftp");
//	                    channel.connect();
//	                    ChannelSftp sftpChannel = (ChannelSftp) channel;
//	
//	                   
//	                    // Cerco file che finiscono per _AV.zip
//	                    log.info("Cerco tracciati di Avvisatura digitale...");
//	                    Vector<ChannelSftp.LsEntry> entryList = sftpChannel.ls(stazione.getId_intermediario() + "*_AV.zip");
//	                    log.info("Trovati " + entryList.size() + " tracciati.");
//	                    for(ChannelSftp.LsEntry entry : entryList) {
//	                        int esito = 0;
//	                       
//	                        ZipInputStream zis = new ZipInputStream(sftpChannel.get(entry.getFilename()));
//	                        // Nello zip mi aspetto che ci sia un solo file XML
//	                        // Se non c'e' niente o non si apre, do errore
//	                        try {
//	                            ZipEntry zipentry = zis.getNextEntry();
//	                        } catch (IOException e1) {
//	                            esito = 1;
//	                            e1.printStackTrace();
//	                        }
//	                       
//	                        // Dal primo file che trovo, leggo la lista degli avvisi
//	                        try {
//	                            ListaAvvisiDigitali listaAvvisiDigitali = NdpUtils.toListaAvvisiDigitali(zis);
//	                        } catch (JAXBException | SAXException e) {
//	                            esito = 3;
//	                            e.printStackTrace();
//	                        }
//	                       
//	                        // TODO
//	                    }
//	                    sftpChannel.exit();
//	                    session.disconnect();
//	                } catch (JSchException e) {
//	                    e.printStackTrace();
//	                } catch (SftpException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	        } catch (SQLException e2) {
//	            // TODO Auto-generated catch block
//	            e2.printStackTrace();
//	        }
//	        return "Avvisatura digitale ok";
//		} catch (Exception e) {
//			log.error("Avvisatura digitale Fallita", e);
//			aggiornaSondaKO(batch_avvisatura_digitale, e, bd);
//			return "Avvisatura digitale#" + e.getMessage();
//		} finally {
//			BatchManager.stopEsecuzione(bd, batch_avvisatura_digitale);
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//		}
//    	return null;
//	    }




}
