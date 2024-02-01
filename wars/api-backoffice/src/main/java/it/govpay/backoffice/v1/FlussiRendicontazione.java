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
package it.govpay.backoffice.v1;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.FlussiRendicontazioneController;
import it.govpay.core.beans.Costanti;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
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
        return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders, null, idFlusso, null);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findFlussiRendicontazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("incassato") Boolean incassato, @QueryParam("idFlusso") String idFlusso, @QueryParam("stato") String stato, @QueryParam("iuv") String iuv, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati, @QueryParam("escludiObsoleti") Boolean escludiObsoleti){
        this.buildContext();
        return this.controller.findFlussiRendicontazione(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, dataDa, dataA, idDominio, incassato, idFlusso, stato, iuv, metadatiPaginazione, maxRisultati, escludiObsoleti);
    }

    @GET
    @Path("/{idDominio}/{idFlusso}")

    @Produces({ "application/xml", "application/json" })
    public Response getFlussoRendicontazioneByIdEData(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idFlusso") String idFlusso){
        this.buildContext();
        //Per retrocompatibilita, controllo se mi stanno invocando /{idFlusso}/{dataOraFlusso}
        try {
        	SimpleDateFormatUtils.getDataDaConTimestamp(idFlusso, "dataOraFlusso");
        	return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders, null, idDominio, idFlusso);

        } catch(ValidationException e) {
        	return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders, idDominio, idFlusso, null);
        }
    }

    @GET
    @Path("/{idDominio}/{idFlusso}/{dataOraFlusso}")

    @Produces({ "application/xml", "application/json" })
    public Response getFlussoRendicontazioneByDominioIdEData(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idFlusso") String idFlusso, @PathParam("dataOraFlusso") String dataOraFlusso){
        this.buildContext();
        return this.controller.getFlussoRendicontazione(this.getUser(), uriInfo, httpHeaders,  idDominio, idFlusso,  dataOraFlusso);
    }

}


