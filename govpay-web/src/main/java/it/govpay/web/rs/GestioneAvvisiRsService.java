/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.util.ArrayList;

import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.CtNumeroAvviso;
import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.InformazioniVersamento;
import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.report.AvvisiDiPagamento;
import it.govpay.report.dto.DocumentoDiPagamentoDTO;
import it.govpay.rs.avviso.RichiestaAvviso;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.ThreadContext;

@Path("/avvisi")
public class GestioneAvvisiRsService {
	
	@Inject
	AnagraficaEJB anagraficaEjb;
	
	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;
	
	@POST
	@Path("/nuovoAvviso")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response generaAvviso(RichiestaAvviso richiestaAvviso) throws GovPayException {
		ThreadContext.put("proc", "GeneraAvviso");
		ThreadContext.put("dom", richiestaAvviso.getIdentificativoBeneficiario());
		ThreadContext.put("iuv", richiestaAvviso.getIuv());
		ThreadContext.put("ccp", null);
		
		AvvisiDiPagamento a = new AvvisiDiPagamento();
		DocumentoDiPagamentoDTO doc = new DocumentoDiPagamentoDTO();

		EnteCreditoreModel creditoreModel = anagraficaEjb.getCreditoreByIdLogico(richiestaAvviso.getIdentificativoBeneficiario());
		doc.setEnte(creditoreModel);
		doc.setRichiestaAvviso(richiestaAvviso);

		ScadenzarioModel scadenzario = anagraficaEjb.getScadenzario(anagraficaEjb.getTributoById(creditoreModel.getIdEnteCreditore(), richiestaAvviso.getTipoDebito()));
		
		String idStazionePa = scadenzario.getIdStazione();
		String applicationCode = idStazionePa.substring(idStazionePa.indexOf("_") + 1);
		
		InformazioniVersamento informazioniVersamento = new InformazioniVersamento();
		informazioniVersamento.setCodiceIdentificativoEnte(creditoreModel.getIdFiscale());
		informazioniVersamento.setImportoVersamento(richiestaAvviso.getImportoTotaleDaVersare());
		CtNumeroAvviso numeroAvviso = new CtNumeroAvviso();
		numeroAvviso.setApplicationCode(applicationCode);
		numeroAvviso.setAuxDigit("0");
		numeroAvviso.setIUV(richiestaAvviso.getIuv());
		informazioniVersamento.setNumeroAvviso(numeroAvviso);
		doc.setInformazioni(informazioniVersamento);
		ArrayList<DocumentoDiPagamentoDTO> docs = new ArrayList<DocumentoDiPagamentoDTO>();
		docs.add(doc);
		
		byte[] pdf = a.creaPdfDocumentoDiPagamento(docs);
		
        return Response.ok(pdf).build();
	}
}

