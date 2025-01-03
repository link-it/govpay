/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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


import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.RendicontazioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/rendicontazioni")

public class Rendicontazioni extends BaseRsServiceV1{


	private RendicontazioniController controller = null;

	public Rendicontazioni() {
		super("rendicontazioni");
		this.controller = new RendicontazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findRendicontazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("flussoRendicontazione.dataFlussoDa") String flussoRendicontazioneDataFlussoDa, @QueryParam("flussoRendicontazione.dataFlussoA") String flussoRendicontazioneDataFlussoA, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("idFlusso") String idFlusso, @QueryParam("iuv") String iuv, @QueryParam("direzione") List<String> direzione, @QueryParam("divisione") List<String> divisione, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati, @QueryParam("escludiObsoleti") @DefaultValue(value="true") Boolean escludiObsoleti){
    	this.buildContext();
        return this.controller.findRendicontazioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, flussoRendicontazioneDataFlussoDa, flussoRendicontazioneDataFlussoA, dataDa, dataA, idDominio, idFlusso, iuv, direzione, divisione, metadatiPaginazione, maxRisultati, escludiObsoleti);
    }

}


