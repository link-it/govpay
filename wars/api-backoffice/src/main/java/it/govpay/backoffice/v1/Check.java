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
package it.govpay.backoffice.v1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.sonde.ParametriSonda;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.Sonda.StatoSonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaCoda;
import org.slf4j.Logger;

import it.govpay.backoffice.v1.sonde.DettaglioSonda;
import it.govpay.backoffice.v1.sonde.DettaglioSonda.TipoSonda;
import it.govpay.backoffice.v1.sonde.ElencoSonde;
import it.govpay.backoffice.v1.sonde.SommarioSonda;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.core.business.Operazioni;

@Path("/sonde")
public class Check {

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonde() {
		BasicBD bd = null;
		Logger log = LoggerWrapperFactory.getLogger(Check.class);
		
		try {
			
			ElencoSonde elenco = new ElencoSonde();
			
			
		
			try {
				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
				
				SommarioSonda dettaglioSonda = new SommarioSonda();
				dettaglioSonda.setStato(0);
				dettaglioSonda.setDescrizioneStato("Servizio database raggiungibile");
				dettaglioSonda.setNome("check-db");
				elenco.getItems().add(dettaglioSonda);
				
				List<Sonda> sonde = SondaFactory.findAll(bd.getConnection(), bd.getJdbcProperties().getDatabase());

				for(Sonda sonda: sonde) {
					sonda.getStatoSonda();
					SommarioSonda sommarioSonda = new SommarioSonda();
					StatoSonda statoSonda = sonda.getStatoSonda();
					ParametriSonda parametri = sonda.getParam();
					parametri.getDatiCheck();

					try (
							StringWriter writer  = new StringWriter();
							PrintWriter printWriter = new PrintWriter(writer); 	
							){
						parametri.getDatiCheck().list(printWriter);
						StringBuffer sb = new StringBuffer(parametri.getNome());
						sb.append(": ").append(writer.getBuffer().toString());
						String msg = sb.toString();
						log.debug(msg);
					}

					sommarioSonda.setNome(parametri.getNome());
					try {
						sommarioSonda.setStato(statoSonda.getStato());
						sommarioSonda.setDescrizioneStato(statoSonda.getDescrizione());
					} catch(Throwable t) {
						sommarioSonda.setStato(2);
						sommarioSonda.setDescrizioneStato("Impossibile acquisire lo stato della sonda: " + t);
						log.error("Impossibile acquisire lo stato della sonda", t);
					}
					elenco.getItems().add(sommarioSonda);
				}
			} catch(ServiceException e) {
				SommarioSonda dettaglioSonda = new SommarioSonda();
				dettaglioSonda.setStato(2);
				dettaglioSonda.setDescrizioneStato("Servizio database non disponibile: " + e.getMessage());
				dettaglioSonda.setNome("check-db");
				elenco.getItems().add(dettaglioSonda);
				log.error("Errore durante la verifica delle sonde", e);
			} finally {
				if(bd!= null) bd.closeConnection();
			}
			return Response.ok(elenco).build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} 
	}

	private Sonda getSonda(BasicBD bd, String nome) throws SondaException, ServiceException, NotFoundException {
		Sonda sonda = SondaFactory.get(nome, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		if(sonda == null)
			throw new NotFoundException("Sonda con nome ["+nome+"] non configurata");
		if(Operazioni.CHECK_NTFY.equals(nome)) {
			long num = -1;
			NotificheBD notBD = new NotificheBD(bd);
			num = notBD.countNotificheInAttesa();
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		if(Operazioni.CHECK_NTFY_APP_IO.equals(nome)) {
			long num = -1;
			((SondaCoda)sonda).aggiornaStatoSonda(num, bd.getConnection(), bd.getJdbcProperties().getDatabase());
		}
		
		return sonda;
	}

	@GET
	@Path("/{nome}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response verificaSonda(@PathParam(value = "nome") String nome) {
		BasicBD bd = null;
		Logger log = LoggerWrapperFactory.getLogger(Check.class);
		
		if(nome.equals("check-db")) {
			DettaglioSonda dettaglioSonda = new DettaglioSonda(TipoSonda.Sconosciuto);
			dettaglioSonda.setId("check-db");
			dettaglioSonda.setNome("Controllo operativita del database");
			dettaglioSonda.setDataUltimoCheck(new Date());
			try {
				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
				
				dettaglioSonda.setStato(0);
				dettaglioSonda.setDescrizioneStato("Servizio database raggiungibile");
				
				return Response.ok(dettaglioSonda).build();
			} catch(Exception e) {
				dettaglioSonda.setStato(2);
				dettaglioSonda.setDescrizioneStato("Servizio database non disponibile: " + e.getMessage());
				dettaglioSonda.setNome("check-db");
				return Response.ok(dettaglioSonda).build();
			} finally {
				if(bd!= null) bd.closeConnection();
			}
		}
		try {
			try {
				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), true);
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());

				DettaglioSonda dettaglioSonda = null;
				try {
					Sonda sonda = this.getSonda(bd, nome);
					dettaglioSonda = new DettaglioSonda(sonda.getClass());
					ParametriSonda param = sonda.getParam();
					
					dettaglioSonda.setId(nome);
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
				} catch (NotFoundException t){
					return Response.status(404).entity("Sonda con nome ["+nome+"] non configurata").build();
				} catch (Throwable t){
					dettaglioSonda = new DettaglioSonda(TipoSonda.Sconosciuto);
					dettaglioSonda.setId(nome);
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

	public class EsitoVerifica {
		private Date ultimo_aggiornamento;
		private Integer codice_stato;
		private String operazione_eseguita;
		private String errore_rilevato;
		public Integer getCodice_stato() {
			return this.codice_stato;
		}
		public void setCodice_stato(Integer codice_stato) {
			this.codice_stato = codice_stato;
		}
		public String getOperazione_eseguita() {
			return this.operazione_eseguita;
		}
		public void setOperazione_eseguita(String operazione_eseguita) {
			this.operazione_eseguita = operazione_eseguita;
		}
		public String getErrore_rilevato() {
			return this.errore_rilevato;
		}
		public void setErrore_rilevato(String errore_rilevato) {
			this.errore_rilevato = errore_rilevato;
		}
		public Date getUltimo_aggiornamento() {
			return this.ultimo_aggiornamento;
		}
		public void setUltimo_aggiornamento(Date ultimo_aggiornamento) {
			this.ultimo_aggiornamento = ultimo_aggiornamento;
		}

		@Override
		public String toString() {
			if(this.codice_stato == 0) return "OK";
			if(this.codice_stato == 1) return "WARN: STATO NON VERIFICATO";
			if(this.codice_stato == 2) return "FAIL: [" + this.operazione_eseguita + "] " + this.errore_rilevato;
			return "ERRORE!!";
		}
	}

}

