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
package it.govpay.web.rs.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.model.Anagrafica;
import it.govpay.model.Applicazione;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.web.rs.v1.beans.PagamentoInAttesa;

@Path("/v1/versamenti")
public class PagamentiInAttesa extends BaseRsServiceV1 {
	
	public static final String NOME_SERVIZIO = "pagamentiAttesa";
	
	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response inserisciIncasso(PagamentoInAttesa pia, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		String methodName = "inserisciIncasso"; 
		try{
			Applicazione applicazione = new UtentiDAO().getApplicazione(getPrincipal());
			
			Versamento versamento = new Versamento();
			versamento.setAggiornabile(true);
			Anagrafica anagraficaDebitore = new Anagrafica();
			anagraficaDebitore.setCap(pia.getPagatore().getCap());
			anagraficaDebitore.setCellulare(pia.getPagatore().getCellulare());
			anagraficaDebitore.setCivico(pia.getPagatore().getCivico());
			anagraficaDebitore.setCodUnivoco(pia.getPagatore().getIdentificativoUnivoco());
			anagraficaDebitore.setEmail(pia.getPagatore().getEmail());
			anagraficaDebitore.setIndirizzo(pia.getPagatore().getIndirizzo());
			anagraficaDebitore.setLocalita(pia.getPagatore().getLocalita());
			anagraficaDebitore.setNazione(pia.getPagatore().getNazione());
			anagraficaDebitore.setProvincia(pia.getPagatore().getProvincia());
			anagraficaDebitore.setRagioneSociale(pia.getPagatore().getDenominazione());
			versamento.setAnagraficaDebitore(anagraficaDebitore);
			versamento.setIdApplicazione(applicazione.getId());
			CausaleSemplice causale = versamento.new CausaleSemplice();
			causale.setCausale(pia.getCausale());
			versamento.setCausaleVersamento(causale);
		//	versamento.setCodVersamentoEnte(pia.get);
//			versamento.setDataScadenza(pia.getScadenzaPagamento());
//			versamento.setImportoTotale(pia.getImporto());
			//versamento.setIuvProposto(iuvProposto);
		//	versamento.setUo(pia.getIdDominio(), Dominio.EC, bd)
			//CaricaVersamentoDTO caricaVersamentoDTO = new CaricaVersamentoDTO(applicazione, versamento);
			return Response.status(Status.OK).entity(pia).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {

		}
	}
	
}
