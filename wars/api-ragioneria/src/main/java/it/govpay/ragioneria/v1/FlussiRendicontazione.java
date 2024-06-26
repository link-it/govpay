/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v1;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.core.beans.Costanti;
import it.govpay.ragioneria.v1.controller.FlussiRendicontazioneController;
import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/flussiRendicontazione")

public class FlussiRendicontazione extends BaseRsServiceV1{


	private FlussiRendicontazioneController controller = null;

	public FlussiRendicontazione() {
		super("flussiRendicontazione");
		this.controller = new FlussiRendicontazioneController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idFlusso}")

    @Produces({ "application/json", MediaType.APPLICATION_XML })
    public Response getFlussoRendicontazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso){
        this.buildContext();
        return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders,  idFlusso);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findFlussiRendicontazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("idDominio") String idDominio, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("stato") String stato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.flussiRendicontazioneGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, dataDa, dataA, idDominio, stato, metadatiPaginazione, maxRisultati);
    }

    @GET
    @Path("/{idFlusso}/{dataOraFlusso}")

    @Produces({ "application/xml", "application/json" })
    public Response getFlussoRendicontazioneByIdEData(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idFlusso") String idFlusso, @PathParam("dataOraFlusso") String dataOraFlusso){
        this.buildContext();
        return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders,  idFlusso,  dataOraFlusso);
    }

}


