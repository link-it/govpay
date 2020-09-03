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
package it.govpay.web.rs;

import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPTRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.Operazioni;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.model.Intermediario;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.web.rs.sonde.CheckSonda;
import it.govpay.web.rs.sonde.DettaglioSonda;
import it.govpay.web.rs.sonde.DettaglioSonda.TipoSonda;
import it.govpay.web.rs.sonde.ElencoSonde;
import it.govpay.web.rs.sonde.SommarioSonda;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Server;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;
import org.openspcoop2.utils.sonde.ParametriSonda;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.Sonda.StatoSonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaCoda;

@Path("/check")
public class Check {

	@GET
	@Path("/sonda")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonde() {
		BasicBD bd = null;
		Logger log = LogManager.getLogger();
		try {
			try {
				bd = BasicBD.newInstance(UUID.randomUUID().toString());
				
				List<CheckSonda> listaCheckSonda = CheckSonda.getListaCheckSonda();
				ElencoSonde elenco = new ElencoSonde();
				for(CheckSonda checkSonda: listaCheckSonda) {
					SommarioSonda sommarioSonda = new SommarioSonda();
					sommarioSonda.setNome(checkSonda.getName());
					try {
						Sonda sonda = getSonda(bd, checkSonda);
						StatoSonda statoSonda = sonda.getStatoSonda();
						sommarioSonda.setStato(statoSonda.getStato());
						sommarioSonda.setDescrizioneStato(statoSonda.getDescrizione());
					} catch(Throwable t) {
						sommarioSonda.setStato(2);
						sommarioSonda.setDescrizioneStato("Impossibile acquisire lo stato della sonda: " + t);
						log.error("Impossibile acquisire lo stato della sonda", t);
					}
					elenco.getItems().add(sommarioSonda);
				}
				return Response.ok(elenco).build();
			} catch(ServiceException e) {
				log.error("Errore durante la verifica delle sonde", e);
				throw new Exception("Errore durante la verifica delle sonde");
			} finally {
				if(bd!= null) bd.closeConnection();
			}
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} 
	}
	
	private Sonda getSonda(BasicBD bd, CheckSonda checkSonda) throws SondaException, ServiceException, NotFoundException {
		Sonda sonda = SondaFactory.get(checkSonda.getName(), bd.getConnection(), bd.getJdbcProperties().getDatabase());
		if(sonda == null)
			throw new NotFoundException("Sonda con nome ["+checkSonda.getName()+"] non configurata");
		if(checkSonda.isCoda()) {
			long num = -1;
			if(Operazioni.check_ntfy.equals(checkSonda.getName())) {
				NotificheBD notBD = new NotificheBD(bd);
				num = notBD.countNotificheInAttesa();
			} else if(Operazioni.check_tracciati.equals(checkSonda.getName())) {
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				TracciatoFilter filter = tracciatiBD.newFilter();
				
				filter.addStatoTracciato(StatoTracciatoType.IN_CARICAMENTO);
				filter.addStatoTracciato(StatoTracciatoType.NUOVO);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, -6); //TODO parametrizzare
				
				filter.setDataCaricamentoMax(cal.getTime());
				num = tracciatiBD.count(filter);
			}
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		return sonda;
	}
	
	@GET
	@Path("/sonda/{nome}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonda(@PathParam(value = "nome") String nome) {
		BasicBD bd = null;
		Logger log = LogManager.getLogger();
		try {
			try {
				bd = BasicBD.newInstance(UUID.randomUUID().toString());
				CheckSonda checkSonda = CheckSonda.getCheckSonda(nome);
				
				if(checkSonda == null)
					return Response.status(404).entity("Sonda con nome ["+nome+"] non configurata").build();
				
				DettaglioSonda dettaglioSonda = null;
				try {
					Sonda sonda = getSonda(bd, checkSonda);
					dettaglioSonda = new DettaglioSonda(sonda.getClass());
					ParametriSonda param = sonda.getParam();
					dettaglioSonda.setNome(param.getNome());
					
					StatoSonda statoSonda = sonda.getStatoSonda();
					dettaglioSonda.setStato(statoSonda.getStato());
					dettaglioSonda.setDescrizioneStato(statoSonda.getDescrizione());
					
					if(statoSonda.getStato() == 0) dettaglioSonda.setDurataStato(param.getDataOk());
					if(statoSonda.getStato() == 1) dettaglioSonda.setDurataStato(param.getDataWarn());
					if(statoSonda.getStato() == 2) dettaglioSonda.setDurataStato(param.getDataError());
					
					dettaglioSonda.setDataUltimoCheck(param.getDataUltimoCheck());
					dettaglioSonda.setSogliaError(param.getSogliaError());
					dettaglioSonda.setSogliaWarn(param.getSogliaWarn());
				} catch (Throwable t){
					dettaglioSonda = new DettaglioSonda(TipoSonda.Sconosciuto);
					dettaglioSonda.setNome(nome);
					dettaglioSonda.setStato(2);
					dettaglioSonda.setDescrizioneStato("Impossibile acquisire lo stato della sonda: " + t);
					log.error("Impossibile acquisire lo stato della sonda", t);
				}
				return Response.ok(dettaglioSonda).build();
			} catch(Exception e) {
				log.error("Errore durante la verifica della sonda " + nome, e);
				throw new Exception("Errore durante la verifica della sonda " + nome);
			} finally {
				if(bd!= null) bd.closeConnection();
			}
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} 
	}


	@GET
	@Path("/db")
	public Response verificaDB() {
		BasicBD bd = null;
		Logger log = LogManager.getLogger();
		try {
			DominiBD dominiBD = null;
			try {
				bd = BasicBD.newInstance(UUID.randomUUID().toString());
				dominiBD = new DominiBD(bd);
				dominiBD.count(dominiBD.newFilter());
			} catch(Exception e) {
				log.error("Errore di connessione al database", e);
				throw new Exception("Errore di connessione al database");
			} finally {
				if(bd!= null) bd.closeConnection();
			}
			return Response.ok().build();

		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} 
	}
	
	@GET
	@Path("/pdd")
	public Response verificaPDD(
			@QueryParam(value = "matchString") String matchString) {
		Logger log = LogManager.getLogger();
		try {
			try {
				URL url = GovpayConfig.getInstance().getUrlPddVerifica();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				String checkResult = null;
				conn.connect();

				int responseCode = conn.getResponseCode();

				String bodyResponse = null;

				if(responseCode > 299) {
					checkResult = "Ottenuto response code ["+responseCode+"] durante la connessione a ["+url+"]";
				} 

				if(matchString != null) {
					checkResult = null;

					if(responseCode < 300) {
						if(conn.getInputStream() != null) {
							bodyResponse = new String(conn.getInputStream() != null ? IOUtils.toByteArray(conn.getInputStream()) : new byte[]{});
						}
					} else {
						if(conn.getErrorStream() != null) {
							bodyResponse = new String(conn.getErrorStream() != null ? IOUtils.toByteArray(conn.getErrorStream()) : new byte[]{});
						}
					}

					if(bodyResponse == null || !bodyResponse.contains(matchString))
						checkResult = "Ottenuta risposta che non contiene la matchString ["+matchString+"] durante la connessione a ["+url+"]";
				}

				if(checkResult != null)
					throw new Exception(checkResult);

			} catch(Exception e) {
				log.error("Errore di connessione alla PDD", e);
				throw new Exception("Errore di connessione alla PDD: " + e.getMessage());
			}

			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} 
	}
	
	public class EsitoVerifica {
		private Date ultimo_aggiornamento;
		private Integer codice_stato;
		private String operazione_eseguita;
		private String errore_rilevato;
		public Integer getCodice_stato() {
			return codice_stato;
		}
		public void setCodice_stato(Integer codice_stato) {
			this.codice_stato = codice_stato;
		}
		public String getOperazione_eseguita() {
			return operazione_eseguita;
		}
		public void setOperazione_eseguita(String operazione_eseguita) {
			this.operazione_eseguita = operazione_eseguita;
		}
		public String getErrore_rilevato() {
			return errore_rilevato;
		}
		public void setErrore_rilevato(String errore_rilevato) {
			this.errore_rilevato = errore_rilevato;
		}
		public Date getUltimo_aggiornamento() {
			return ultimo_aggiornamento;
		}
		public void setUltimo_aggiornamento(Date ultimo_aggiornamento) {
			this.ultimo_aggiornamento = ultimo_aggiornamento;
		}
		
		@Override
		public String toString() {
			if(codice_stato == 0) return "OK";
			if(codice_stato == 1) return "WARN: STATO NON VERIFICATO";
			if(codice_stato == 2) return "FAIL: [" + operazione_eseguita + "] " + errore_rilevato;
			return "ERRORE!!";
		}
	}
	
	@GET
	@Path("/{codDominio}")
	public Response verificaDominioJson(@PathParam(value = "codDominio") String codDominio) {
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			Service service = new Service();
			service.setName("Check");
			ctx.getTransaction().setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			operation.setName("VerificaDominio");
			ctx.getTransaction().setOperation(operation);
			
			Server server = new Server();
			server.setName(GpContext.GovPay);
			ctx.getTransaction().setServer(server);
			
			Actor to = new Actor();
			to.setName(GpContext.GovPay);
			to.setType(GpContext.TIPO_SOGGETTO_GOVPAY);
			ctx.getTransaction().setTo(to);
			
			ThreadContext.put("op", ctx.getTransactionId());
			GpThreadLocal.set(ctx);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			EsitoVerifica esito = new EsitoVerifica();
			try {
				Dominio d = AnagraficaManager.getDominio(bd, codDominio);
				Stazione stazione = d.getStazione(bd);
				Intermediario intermediario = stazione.getIntermediario(bd);
				
				try {
					NodoClient client = new NodoClient(intermediario, bd);
					NodoChiediListaPendentiRPT nodoChiediListaPendentiRPT = new NodoChiediListaPendentiRPT();
					nodoChiediListaPendentiRPT.setDimensioneLista(BigInteger.ONE);
					nodoChiediListaPendentiRPT.setIdentificativoDominio(codDominio);
					nodoChiediListaPendentiRPT.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
					nodoChiediListaPendentiRPT.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
					nodoChiediListaPendentiRPT.setRangeDa(new Date());
					nodoChiediListaPendentiRPT.setRangeA(new Date());
					nodoChiediListaPendentiRPT.setPassword(stazione.getPassword());
					NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPT2 = client.nodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT, null);
					if(nodoChiediListaPendentiRPT2.getFault() != null) {
						esito.codice_stato=2;
						esito.errore_rilevato=nodoChiediListaPendentiRPT2.getFault().getFaultString();
						esito.ultimo_aggiornamento=new Date();
					} else {
						esito.codice_stato=0;
						esito.ultimo_aggiornamento=new Date();
					}
				} catch (ClientException ce){
					esito.codice_stato=2;
					esito.errore_rilevato="Errore pagoPA: " + ce.getMessage();
					esito.ultimo_aggiornamento=new Date();
				} catch (GovPayException e) {
					// Non la lancia nessuno...
					esito.codice_stato=2;
					esito.errore_rilevato="Errore pagoPA: " + e.getMessage();
					esito.ultimo_aggiornamento=new Date();				}
				
			} catch(NotFoundException e) {
				return Response.status(404).build();
			} 
			return Response.ok(esito).build();
		} catch (ServiceException e) {
			return Response.status(500).entity(e.getMessage()).build();
		} finally {
			if(bd!= null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}
