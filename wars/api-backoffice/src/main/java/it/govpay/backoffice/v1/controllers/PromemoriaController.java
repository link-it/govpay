package it.govpay.backoffice.v1.controllers;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaPromemoria;
import it.govpay.backoffice.v1.beans.PromemoriaIndex;
import it.govpay.backoffice.v1.beans.StatoPromemoria;
import it.govpay.backoffice.v1.beans.TipoPromemoria;
import it.govpay.backoffice.v1.beans.converter.PromemoriaConverter;
import it.govpay.bd.model.Promemoria;
import it.govpay.core.dao.pagamenti.PromemoriaDAO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class PromemoriaController extends BaseController {

     public PromemoriaController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response findPromemoria(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String dataDa, String dataA, String stato, String tipo) {
    	String methodName = "findPromemoria";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			ListaPromemoriaDTO listaPromemoriaDTO = new ListaPromemoriaDTO(user);
			
			listaPromemoriaDTO.setLimit(risultatiPerPagina);
			listaPromemoriaDTO.setPagina(pagina);
			
			if(stato != null) {
				StatoPromemoria statoPromemoria = StatoPromemoria.fromValue(stato);
				if(statoPromemoria != null)
				switch (statoPromemoria) {
				case ANNULLATO:
					listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.ANNULLATO);
					break;
				case DA_SPEDIRE:
					listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.DA_SPEDIRE);
					break;
				case FALLITO:
					listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.FALLITO);
					break;
				case SPEDITO:
					listaPromemoriaDTO.setStato(it.govpay.model.Promemoria.StatoSpedizione.SPEDITO);
					break;
				}
			}
			
			if(tipo != null) {
				TipoPromemoria tipoPromemoria = TipoPromemoria.fromValue(tipo);
				if(tipoPromemoria != null)
				switch (tipoPromemoria) {
				case AVVISO_PAGAMENTO:
					listaPromemoriaDTO.setTipo(it.govpay.model.Promemoria.TipoPromemoria.AVVISO);
					break;
				case RICEVUTA_TELEMATICA:
					listaPromemoriaDTO.setTipo(it.govpay.model.Promemoria.TipoPromemoria.RICEVUTA);
					break;
				}
			}

			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaPromemoriaDTO.setDataDa(dataDaDate);
			}
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaPromemoriaDTO.setDataA(dataADate);
			}
			
//			// Autorizzazione sui domini
//			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
//			if(idDomini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}
//			listaPromemoriaDTO.setIdDomini(idDomini);
//			// autorizzazione sui tipi pendenza
//			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
//			if(idTipiVersamento == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
//			}
//			listaPromemoriaDTO.setIdTipiVersamento(idTipiVersamento);
			
			// INIT DAO

			PromemoriaDAO notificheDAO = new PromemoriaDAO(); 

			// CHIAMATA AL DAO

			ListaPromemoriaDTOResponse listaPromemoriaDTOResponse = notificheDAO.listaPromemoria(listaPromemoriaDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<PromemoriaIndex> results = new ArrayList<>();
			for(Promemoria notifica: listaPromemoriaDTOResponse.getResults()) {
				results.add(PromemoriaConverter.toRsModelIndex(notifica));
			}

			ListaPromemoria response = new ListaPromemoria(results, this.getServicePath(uriInfo),
					listaPromemoriaDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
}


