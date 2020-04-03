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

import it.govpay.backoffice.v1.beans.ListaNotifiche;
import it.govpay.backoffice.v1.beans.NotificaIndex;
import it.govpay.backoffice.v1.beans.StatoNotifica;
import it.govpay.backoffice.v1.beans.TipoNotifica;
import it.govpay.backoffice.v1.beans.converter.NotificheConverter;
import it.govpay.bd.model.Notifica;
import it.govpay.core.dao.pagamenti.NotificheDAO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA; 



public class NotificheController extends BaseController {

     public NotificheController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findNotifiche(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String dataDa, String dataA, String stato, String tipo) {
    	String methodName = "findNotifiche";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			ListaNotificheDTO listaNotificheDTO = new ListaNotificheDTO(user);
			
			listaNotificheDTO.setLimit(risultatiPerPagina);
			listaNotificheDTO.setPagina(pagina);
			
			if(stato != null) {
				StatoNotifica statoNotifica = StatoNotifica.fromValue(stato);
				if(statoNotifica != null)
				switch (statoNotifica) {
				case ANNULLATA:
					listaNotificheDTO.setStato(it.govpay.model.Notifica.StatoSpedizione.ANNULLATA);
					break;
				case DA_SPEDIRE:
					listaNotificheDTO.setStato(it.govpay.model.Notifica.StatoSpedizione.DA_SPEDIRE);
					break;
				case SPEDITA:
					listaNotificheDTO.setStato(it.govpay.model.Notifica.StatoSpedizione.SPEDITO);
					break;
				}
			}
			
			if(tipo != null) {
				TipoNotifica tipoNotifica = TipoNotifica.fromValue(tipo);
				if(tipoNotifica != null)
				switch (tipoNotifica) {
				case ANNULLAMENTO:
					listaNotificheDTO.setTipo(it.govpay.model.Notifica.TipoNotifica.ANNULLAMENTO);
					break;
				case ATTIVAZIONE:
					listaNotificheDTO.setTipo(it.govpay.model.Notifica.TipoNotifica.ATTIVAZIONE);
					break;
				case FALLIMENTO:
					listaNotificheDTO.setTipo(it.govpay.model.Notifica.TipoNotifica.FALLIMENTO);
					break;
				case RICEVUTA:
					listaNotificheDTO.setTipo(it.govpay.model.Notifica.TipoNotifica.RICEVUTA);
					break;
				}
			}

			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaNotificheDTO.setDataDa(dataDaDate);
			}
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaNotificheDTO.setDataA(dataADate);
			}
			
//			// Autorizzazione sui domini
//			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
//			if(idDomini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}
//			listaNotificheDTO.setIdDomini(idDomini);
//			// autorizzazione sui tipi pendenza
//			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
//			if(idTipiVersamento == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
//			}
//			listaNotificheDTO.setIdTipiVersamento(idTipiVersamento);
			
			// INIT DAO

			NotificheDAO notificheDAO = new NotificheDAO(); 

			// CHIAMATA AL DAO

			ListaNotificheDTOResponse listaNotificheDTOResponse = notificheDAO.listaNotifiche(listaNotificheDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<NotificaIndex> results = new ArrayList<>();
			for(Notifica notifica: listaNotificheDTOResponse.getResults()) {
				results.add(NotificheConverter.toRsModelIndex(notifica));
			}

			ListaNotifiche response = new ListaNotifiche(results, this.getServicePath(uriInfo),
					listaNotificheDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


