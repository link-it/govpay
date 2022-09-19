package it.govpay.netpay.v1.api;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.govpay.netpay.v1.beans.GetRTResponse;

/**
 * GovPay - Net@Pay API RT e Attiva Pagamento
 *
 * <p>API di integrazione a Net@Pay esposte da GovPay per:  - Consultare le ricevute di pagamento  - Avvio di un pagamento di pendenze   ## Ricevute Telematiche  La piattaforma mette a disposizione un servizio per la lettura delle ricevute per i pagamenti andati a buon fine.   ## Attivazione Pagamenti  L'operazione consente di avviare una sessione di pagamento per una pendenza.
 *
 */
@Path("/")
public interface RicevuteApi  {

    /**
     * Consultazione di una ricevuta di pagamento
     *
     */
    @POST
    @Path("/get_rt")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(summary = "Consultazione di una ricevuta di pagamento", tags={ "Ricevute" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Oggetto RegisterPaymentResponse contenente il risultato della presa in carico della notifica", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetRTResponse.class))),
        @ApiResponse(responseCode = "401", description = "Richiesta non autenticata"),
        @ApiResponse(responseCode = "403", description = "Richiesta non autorizzata"),
        @ApiResponse(responseCode = "500", description = "Servizio non disponibile") })
    public Response getRt(InputStream is);
}
