/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.pagamento.v2.controller;

import java.util.Arrays;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import org.apache.commons.lang3.ArrayUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAcceptableException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.LinguaSecondaria;
import it.govpay.pagamento.v2.beans.StatoAvviso;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;

public class AvvisiController extends BaseController {

	public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}



	@SuppressWarnings("unchecked")
	public Response avvisiIdDominioIuvGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String numeroAvviso, String idDebitore, String UUID, String gRecaptchaResponse, String linguaSecondaria) {
		String methodName = "avvisiIdDominioIuvGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, numeroAvviso);
			getAvvisoDTO.setCfDebitore(idDebitore);
			getAvvisoDTO.setIdentificativoCreazionePendenza(UUID);
			getAvvisoDTO.setRecaptcha(gRecaptchaResponse);
			getAvvisoDTO.setVerificaAvviso(true);

			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}

			if(!AuthorizationManager.isDominioAuthorized(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio(),null);
			}

			if(linguaSecondaria != null) {
				LinguaSecondaria linguaSecondariaEnum = LinguaSecondaria.fromValue(linguaSecondaria);
				if(linguaSecondariaEnum != null) {
					switch(linguaSecondariaEnum) {
					case DE:
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.DE);
						break;
					case EN:
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.EN);
						break;
					case FALSE:
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FALSE);
						break;
					case FR:
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FR);
						break;
					case SL:
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.SL);
						break;
					}
				} else {
					throw new ValidationException("Codifica inesistente per linguaSecondaria. Valore fornito [" + linguaSecondaria + "] valori possibili " + ArrayUtils.toString(LinguaSecondaria.values()));
				}
			}

			// il versamento riferito dall'avviso potrebbe non essere ancora stato inserito nel db ma essere in sessione
			Map<String, Versamento> listaIdentificativi = null;
			Map<String, String> listaAvvisi = null;
			Versamento versamentoFromSession = null;
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO) || userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				HttpSession session = this.request.getSession(false);
				if(session!= null) {
					listaIdentificativi = (Map<String, Versamento>) session.getAttribute(BaseController.PENDENZE_CITTADINO_ATTRIBUTE);

					String chiaveAvviso = idDominio+numeroAvviso;
					if(numeroAvviso.length() == 18) {
						listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.AVVISI_CITTADINO_ATTRIBUTE);
					} else {
						listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.IUV_CITTADINO_ATTRIBUTE);
					}

					if(listaAvvisi != null && listaAvvisi.size() > 0) {
						if(listaAvvisi.containsKey(chiaveAvviso)) {
							String chiavePendenza = listaAvvisi.get(chiaveAvviso);

							if(listaIdentificativi.containsKey(chiavePendenza)) {
								versamentoFromSession = listaIdentificativi.get(chiavePendenza);
							}
						}
					}
				}
			}
			getAvvisoDTO.setVersamentoFromSession(versamentoFromSession);

			AvvisiDAO avvisiDAO = new AvvisiDAO();

			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				Avviso avviso = null;
				try {
					GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
					avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				} catch (PendenzaNonTrovataException pnte) {
					avviso = new Avviso();
					avviso.setStato(StatoAvviso.SCONOSCIUTA);
					avviso.setNumeroAvviso(numeroAvviso);
					avviso.setIdDominio(idDominio);
				}
				this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
				return this.handleResponseOk(Response.status(Status.OK).entity(avviso.toJSON(null)),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAcceptableException("Avviso di pagamento non disponibile nel formato indicato nell'header Accept, ricevuto: '"+accept+"', consentiti: {'application/pdf','application/json'}");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

}
