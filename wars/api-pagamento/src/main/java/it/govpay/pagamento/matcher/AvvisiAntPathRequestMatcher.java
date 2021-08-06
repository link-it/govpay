package it.govpay.pagamento.matcher;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.pagamento.v2.controller.BaseController;
import it.govpay.rs.v1.authentication.hardening.matcher.HardeningAntPathRequestMatcher;

public class AvvisiAntPathRequestMatcher extends HardeningAntPathRequestMatcher {

	private static final Log logger = LogFactory.getLog(AvvisiAntPathRequestMatcher.class);
	private static final String PARAMETER_UUID = "UUID";

	public AvvisiAntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive) {
		super(pattern, httpMethod, caseSensitive);
	}

	public AvvisiAntPathRequestMatcher(String pattern, String httpMethod) {
		super(pattern, httpMethod);
	}

	public AvvisiAntPathRequestMatcher(String pattern) {
		super(pattern);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		boolean authorizedPathMatch = this.doMatches(request);

		if(authorizedPathMatch) {
			Hardening setting = this.readSettings();
			String getPathInfo = request.getPathInfo(); // da /public in poi
			String[] splitAvvisi = getPathInfo.split("/avvisi/");

			String idDominio = null;
			String iuv = null;

			if(splitAvvisi != null && splitAvvisi.length > 0) {
				String[]  split = splitAvvisi[1].split("/");

				if(split != null && split.length > 1) {
					// id dominio e' il path in posizione 1
					idDominio = split[0];
					// iuv e' il path in posizione 2
					iuv = split[1];	
				}
			}

			if(setting.isAbilitato()) {
				logger.debug("Applico regole di hardening per l'accesso alla risorsa ["+getPathInfo+"]...");
				// per il servizio di avviso i controlli sono in cascata
				// controllo UUID
				// controllo Recaptcha

				// 1. Autorizzazione per UUID
				String uuid = request.getParameter(PARAMETER_UUID);
				boolean authorizedUUID = false;
				logger.debug("Controllo presenza del parametro "+PARAMETER_UUID+"...");

				try {
					if(uuid != null) {
						logger.debug("Parametro "+PARAMETER_UUID+" trovato, controllo disponibilita' avviso nel sistema...");
						if(idDominio != null && iuv != null) {
							authorizedUUID = authorizedPathMatch && checkDirittiAvviso(request, idDominio, iuv, uuid); //se hai diritto di vedere la risorsa passi altrimenti controllo il captcha
						} else { // parametri non validi
							authorizedUUID = false;
						}
					} else {
						logger.debug("Parametro "+PARAMETER_UUID+" non trovato.");
					}
				}catch(Throwable e) {
					logger.error("Errore durante il check disponibilita' avviso: " + e.getMessage(), e);
					authorizedUUID = false;
				}

				// 2. Controllo del captcha se UUID = null oppure se non ho passato il controllo con UUID
				if(!authorizedUUID) {
					logger.debug("Applico controllo tramite validazione ReCaptcha...");
					authorizedPathMatch = authorizedPathMatch && this.applicaControlloReCaptcha(request, setting);
					logger.debug("Controllo tramite validazione ReCaptcha completato.");
				}

				logger.debug("Controllo accesso alla risorsa ["+request.getPathInfo()+"], regole di hardening applicate con esito ["+(authorizedPathMatch ? "OK" : "KO")+"]");
			} else {
				logger.debug("Regole di hardening disabilitate per l'accesso alla risorsa ["+getPathInfo+"], accesso consentito.");
				authorizedPathMatch = true; // se il controllo e' disabilitato passo
			}

			// se ho richiesto un pdf devo sempre controllare se l'identificativo della pendenza e' tra quelli in sessione 
			if(authorizedPathMatch && this.richiestoPdf(request)) {
				if(idDominio != null && iuv != null) { // controllo presenza dei parametri di identificazione dell'avviso
					try {
						logger.debug("Richiesto avviso in formato pdf, controllo identificativi in sessione...");
						authorizedPathMatch = authorizedPathMatch && checkDirittiAvvisoSessione(request, idDominio, iuv);
					}catch(Throwable e) {
						logger.error("Errore durante il check disponibilita' avviso: " + e.getMessage(), e);
						authorizedPathMatch = false;
					}
				} else { // parametri non validi
					authorizedPathMatch = false;
				}
			} 
		}

		return authorizedPathMatch;
	}

	private boolean richiestoPdf(HttpServletRequest request) {
		String acceptH = request.getHeader("Accept");
		if(acceptH == null) {
			acceptH = request.getHeader("Accept".toUpperCase());
		}
		if(acceptH == null) {
			acceptH = request.getHeader("Accept".toLowerCase());
		}
		if(acceptH == null) {
			acceptH = "";
		}

		return "application/pdf".equals(acceptH.toLowerCase());
	}

	@SuppressWarnings("unchecked")
	private boolean checkDirittiAvviso(HttpServletRequest request, String idDominio,String iuv, String uuid) {
		boolean authorized = false;

		BDConfigWrapper configWrapper = new BDConfigWrapper(UUID.randomUUID().toString(), true);
		VersamentiBD versamentiBD = null;
		try {
			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+", UUID: "+uuid+"] in corso...");
			versamentiBD = new VersamentiBD(configWrapper);
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(SecurityContextHolder.getContext().getAuthentication(), idDominio);

			// nel caso in cui la pendenza sia stata caricata in memoria, puo' non essere ancora presente sul db
			HttpSession session = request.getSession(false);
			if(session!= null) {
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], trovata sessione con id ["+session.getId()+"]");

				String chiaveAvviso = idDominio+iuv;

				Map<String, String> listaAvvisi = null;
				if(iuv.length() == 18) {
					listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.AVVISI_CITTADINO_ATTRIBUTE); 
				} else {
					listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.IUV_CITTADINO_ATTRIBUTE);
				}
				
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], lista avvisi: ["+((listaAvvisi!= null && listaAvvisi.size() > 0 )? (StringUtils.join(listaAvvisi.keySet(), ",")): "non presente")+"]");

				if(listaAvvisi != null && listaAvvisi.size() > 0) {
					if(listaAvvisi.containsKey(chiaveAvviso)) {
						logger.debug("Avviso [idDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] presente in lista avvisi, effettuo controllo uuid...");
						Map<String, Versamento> listaIdentificativi = (Map<String, Versamento>) session.getAttribute(BaseController.PENDENZE_CITTADINO_ATTRIBUTE); 
						String chiavePendenza = listaAvvisi.get(chiaveAvviso);

						if(listaIdentificativi.containsKey(chiavePendenza)) {
							Versamento versamento = listaIdentificativi.get(chiavePendenza);
	
							if(versamento.getIdSessione() != null && uuid.equals(versamento.getIdSessione())) {
								logger.debug("Avviso [idDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+", UUID: "+uuid+"] presente in lista pendenze, autorizzazione concessa.");
								return true;
							}
						}
					}
				}
			}

			if(iuv.length() == 18)
				getAvvisoDTO.setNumeroAvviso(iuv);
			else 
				getAvvisoDTO.setIuv(iuv);
			getAvvisoDTO.setIdentificativoCreazionePendenza(uuid);
			GetAvvisoDTOResponse checkDisponibilitaAvviso = avvisiDAO.checkDisponibilitaAvviso(getAvvisoDTO, versamentiBD);
			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+", UUID: "+uuid+"] completato con esito ["+(checkDisponibilitaAvviso.isFound() ? "accesso consentito" : "accesso negato")+"].");
			authorized = checkDisponibilitaAvviso.isFound();
		} catch(PendenzaNonTrovataException e){
			return false;
		}catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile effettuare il check disponibilita' avviso: "+ e.getMessage(), e);
		}	finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}

		return authorized;
	}

	@SuppressWarnings("unchecked")
	private boolean checkDirittiAvvisoSessione(HttpServletRequest request, String idDominio,String iuv) {
		boolean authorized = false;

		BDConfigWrapper configWrapper = new BDConfigWrapper(UUID.randomUUID().toString(), true);
		VersamentiBD versamentiBD = null;
		try {
			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] in corso...");

			// gli avvisi disponibili potrebbero non essere ancora stati inseriti sul db, faccio prima un check in sessione
			Map<String, Versamento> listaIdentificativi = null;
			Map<String, String> listaAvvisi = null;
			HttpSession session = request.getSession(false);
			String chiaveAvviso = idDominio+iuv;
			if(session!= null) {
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], trovata sessione con id ["+session.getId()+"]");
				listaIdentificativi = (Map<String, Versamento>) session.getAttribute(BaseController.PENDENZE_CITTADINO_ATTRIBUTE); 
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], lista identificativi pendenze: ["+((listaIdentificativi!= null && listaIdentificativi.size() > 0 ) ? (StringUtils.join(listaIdentificativi.keySet(), ",")): "non presente")+"]");
				if(iuv.length() == 18) {
					listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.AVVISI_CITTADINO_ATTRIBUTE); 
				} else {
					listaAvvisi = (Map<String, String>) session.getAttribute(BaseController.IUV_CITTADINO_ATTRIBUTE);
				}
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], lista avvisi: ["+((listaAvvisi!= null && listaAvvisi.size() > 0 ) ? (StringUtils.join(listaAvvisi.keySet(), ",")): "non presente")+"]");
		
				if(listaAvvisi != null && listaAvvisi.size() > 0) {
					if(listaAvvisi.containsKey(chiaveAvviso)) {
						logger.debug("Avviso [idDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] presente in lista avvisi, effettuo controllo id Pendenza...");
						
						String chiavePendenza = listaAvvisi.get(chiaveAvviso);

						if(listaIdentificativi.containsKey(chiavePendenza)) {
							logger.debug("Avviso [idDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] presente in lista pendenze, autorizzazione concessa.");
							return true;
						}
					}
				}
				
				logger.debug("Avviso [idDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] non presente in lista pendenze, effettuo controllo su DB.");
			}

			versamentiBD = new VersamentiBD(configWrapper);
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(SecurityContextHolder.getContext().getAuthentication(), idDominio);
			if(iuv.length() == 18)
				getAvvisoDTO.setNumeroAvviso(iuv);
			else 
				getAvvisoDTO.setIuv(iuv);
			getAvvisoDTO.setFormato(FormatoAvviso.JSON);
			GetAvvisoDTOResponse checkDisponibilitaAvviso = avvisiDAO.getAvviso(getAvvisoDTO, versamentiBD, configWrapper);
			logger.debug("Lettura avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] completata, controllo identificativi consentiti all'utente in corso...");
			String idPendenza = checkDisponibilitaAvviso.getVersamento().getCodVersamentoEnte();
			String idA2A = checkDisponibilitaAvviso.getApplicazione().getCodApplicazione();

			if(session!= null) {
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], trovata sessione con id ["+session.getId()+"]");
				listaIdentificativi = (Map<String, Versamento>) session.getAttribute(BaseController.PENDENZE_CITTADINO_ATTRIBUTE); 
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], lista identificativi pendenze: ["+(listaIdentificativi!= null ? (StringUtils.join(listaIdentificativi.keySet(), ",")): "non presente")+"]");

				if(listaIdentificativi != null && listaIdentificativi.containsKey((idA2A+idPendenza)) ) {
					logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], identificativo [idA2A:"+idA2A+", idPendenza: "+idPendenza+"] presente tra quelli autorizzati: accesso consentito");
					authorized = true;
				} else {
					logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], identificativo [idA2A:"+idA2A+", idPendenza: "+idPendenza+"] non presente in lista: accesso negato");
				}
			} else {
				logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"], la sessione corrente e' null");
			}

			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+"] completato con esito ["+(authorized ? "accesso consentito" : "accesso negato")+"].");
		} catch(PendenzaNonTrovataException e){
			return false;
		}catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile effettuare il check disponibilita' avviso: "+ e.getMessage(), e);
		}	finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}

		return authorized;
	}
}
