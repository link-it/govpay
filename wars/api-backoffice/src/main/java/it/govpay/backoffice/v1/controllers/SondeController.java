package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.sonde.ParametriSonda;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaCoda;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaSonde;
import it.govpay.backoffice.v1.beans.Sonda.TipoSonda;
import it.govpay.backoffice.v1.beans.converter.SondeConverter;
import it.govpay.backoffice.v1.sonde.Costanti;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.utils.GovpayConfig;

public class SondeController extends BaseController{

	public SondeController(String nomeServizio, Logger log) {
		super(nomeServizio, log);
	}

	public Response findSonde(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders) {
		String methodName = "findSonde";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		BasicBD bd = null;
		try{
			ListaSonde response = null;
			List<it.govpay.backoffice.v1.beans.Sonda> risultati = new ArrayList<>();
			try {
				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
			
				it.govpay.backoffice.v1.beans.Sonda sondaCheckDB = new it.govpay.backoffice.v1.beans.Sonda(TipoSonda.Sistema);
				sondaCheckDB.setStato(SondeConverter.getStatoSonda(0));
				sondaCheckDB.setDescrizioneStato(Costanti.CHECK_DB_DESCRIZIONE_STATO_OK);
				sondaCheckDB.setNome(SondeConverter.getNomeSonda(Costanti.CHECK_DB));
				sondaCheckDB.setId(Costanti.CHECK_DB);
				sondaCheckDB.setDataUltimoCheck(new Date());
				risultati.add(sondaCheckDB);
				
				List<Sonda> sonde = SondaFactory.findAll(bd.getConnection(), bd.getJdbcProperties().getDatabase());

				for(Sonda sonda: sonde) {
					ParametriSonda parametri = sonda.getParam();
					
					// aggiorno lo stato delle sonde
					sonda = this.updateStatoSonda(bd, parametri.getNome(), sonda);
					parametri = sonda.getParam();
					
					it.govpay.backoffice.v1.beans.Sonda rsModel = SondeConverter.toRsModel(sonda, parametri);

					risultati.add(rsModel);
				}
			} catch(ServiceException e) {
				it.govpay.backoffice.v1.beans.Sonda sondaCheckDB = new it.govpay.backoffice.v1.beans.Sonda(TipoSonda.Sistema);
				sondaCheckDB.setStato(SondeConverter.getStatoSonda(2));
				sondaCheckDB.setDescrizioneStato(MessageFormat.format(Costanti.CHECK_DB_DESCRIZIONE_STATO_DATABASE_NON_DISPONIBILE, e.getMessage()));
				sondaCheckDB.setNome(SondeConverter.getNomeSonda(Costanti.CHECK_DB));
				sondaCheckDB.setId(Costanti.CHECK_DB);
				sondaCheckDB.setDataUltimoCheck(new Date());
				risultati.add(sondaCheckDB);
				log.error("Errore durante la verifica delle sonde", e);
			} finally {
				if(bd!= null) bd.closeConnection();
			}
			
			response = new ListaSonde(risultati, this.getServicePath(uriInfo), (long) risultati.size(), null, null, null);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response getSonda(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String id) {
		String methodName = "getSonda";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		BasicBD bd = null;
		try{ 
			
			it.govpay.backoffice.v1.beans.Sonda response = null;
			
			try {
				if(id.equals(Costanti.CHECK_DB)) {
					response = new it.govpay.backoffice.v1.beans.Sonda(TipoSonda.Sistema);
					response.setId(id);
					response.setNome(SondeConverter.getNomeSonda(id));
					response.setDataUltimoCheck(new Date());
					try {
						bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
						bd.setupConnection(ContextThreadLocal.get().getTransactionId());
						
						response.setStato(SondeConverter.getStatoSonda(0));
						response.setDescrizioneStato(Costanti.CHECK_DB_DESCRIZIONE_STATO_OK);
					} catch(Exception e) {
						response.setStato(SondeConverter.getStatoSonda(2));
						response.setDescrizioneStato(MessageFormat.format(Costanti.CHECK_DB_DESCRIZIONE_STATO_DATABASE_NON_DISPONIBILE, e.getMessage()));
					} finally {
						if(bd!= null) bd.closeConnection();
					}
				} else {
					bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
					bd.setupConnection(ContextThreadLocal.get().getTransactionId());
	
					try {
						Sonda sonda = this.getSonda(bd, id);
						ParametriSonda param = sonda.getParam();
						response = SondeConverter.toRsModel(sonda, param); 
					} catch (NotFoundException t){
						return Response.status(404).entity(MessageFormat.format(Costanti.SONDA_CON_ID_NON_CONFIGURATA, id)).build();
					} catch (Throwable t){
						response = new it.govpay.backoffice.v1.beans.Sonda(TipoSonda.Sconosciuto);
						response.setId(id);
						response.setNome(SondeConverter.getNomeSonda(id));
						response.setStato(SondeConverter.getStatoSonda(2));
						response.setDescrizioneStato(MessageFormat.format(Costanti.SONDA_IMPOSSIBILE_ACQUISIRE_LO_STATO, t));
						log.error("Impossibile acquisire lo stato della sonda", t);
					}
				}
			} catch(Exception e) {
				log.error("Errore durante la verifica della sonda " + id, e);
				throw new Exception("Errore durante la verifica della sonda " + id);
			} finally {
				if(bd!= null) bd.closeConnection();
			}
			
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}
	
	private Sonda getSonda(BasicBD bd, String nome) throws SondaException, ServiceException, NotFoundException {
		Sonda sonda = SondaFactory.get(nome, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		return updateStatoSonda(bd, nome, sonda);
	}

	private Sonda updateStatoSonda(BasicBD bd, String nome, Sonda sonda)
			throws NotFoundException, ServiceException, SondaException {
		if(sonda == null)
			throw new NotFoundException(MessageFormat.format(Costanti.SONDA_CON_ID_NON_CONFIGURATA, nome));
		if(Costanti.CHECK_NTFY.equals(nome)) {
			long num = -1;
			NotificheBD notBD = new NotificheBD(bd);
			notBD.setAtomica(false); // connessione condivisa
			num = notBD.countNotificheInAttesa();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_NTFY_APP_IO.equals(nome)) {
			long num = -1;
			NotificheAppIoBD notificheBD = new NotificheAppIoBD(bd); 
			notificheBD.setAtomica(false); // connessione condivisa
			num = notificheBD.countNotificheInAttesa();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_CHIUSURA_RPT_SCADUTE.equals(nome)) {
			long num = -1;
			RptBD rptBD = new RptBD(bd);
			rptBD.setAtomica(false); // connessione condivisa
			num = rptBD.countRptScadute(null, GovpayConfig.getInstance().getTimeoutPendentiModello3_SANP_24_Mins());
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI.equals(nome)) {
			long num = -1;
			TracciatiNotificaPagamentiBD notificheBD = new TracciatiNotificaPagamentiBD(bd); 
			notificheBD.setAtomica(false); // connessione condivisa
			num = notificheBD.countTracciatiInStatoNonTerminale(null,null,null);
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_GESTIONE_PROMEMORIA.equals(nome)) {
			long num = -1;
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAtomica(false); // connessione condivisa
			long c1 = versamentiBD.countVersamentiConAvvisoDiPagamentoDaSpedire();
			long c2 = versamentiBD.countVersamentiConAvvisoDiScadenzaDaSpedireViaAppIO();
			long c3 = versamentiBD.countVersamentiConAvvisoDiScadenzaDaSpedireViaMail();
			
			num = c1 +c2+ c3;
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_PROMEMORIA.equals(nome)) {
			long num = -1;
			PromemoriaBD promemoriaBD = new PromemoriaBD(bd);
			promemoriaBD.setAtomica(false); // connessione condivisa
			num = promemoriaBD.countPromemoriaDaSpedire();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_RICONCILIAZIONI.equals(nome)) {
			long num = -1;
			IncassiBD incassiBD = new IncassiBD(bd);
			incassiBD.setAtomica(false); // connessione condivisa
			num = incassiBD.countRiconciliazioniDaAcquisire();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI.equals(nome)) {
			long num = -1;
			TracciatiNotificaPagamentiBD notificheBD = new TracciatiNotificaPagamentiBD(bd); 
			notificheBD.setAtomica(false); // connessione condivisa
			num = notificheBD.countTracciatiInStatoNonTerminale(null,null,null);
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Costanti.CHECK_TRACCIATI.equals(nome)) { 
			long num = -1;
			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			tracciatiBD.setAtomica(false); // connessione condivisa
			num = tracciatiBD.countTracciatiDaElaborare();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		
		return sonda;
	}
}
